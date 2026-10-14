package matchuri.backend.groupdecision.support.recommendation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import matchuri.backend.groupdecision.entity.GroupRecommendation;
import matchuri.backend.groupdecision.entity.GroupRecommendationCandidate;
import matchuri.backend.groupdecision.entity.GroupRecommendationCategory;
import matchuri.backend.groupdecision.entity.GroupRoom;
import matchuri.backend.groupdecision.entity.GroupRoomMember;
import matchuri.backend.groupdecision.repository.GroupRecommendationCandidateRepository;
import matchuri.backend.groupdecision.repository.GroupRecommendationCategoryRepository;
import matchuri.backend.groupdecision.repository.GroupRoomMemberRepository;
import matchuri.backend.groupdecision.support.location.GroupLocationManager;
import matchuri.backend.groupdecision.support.recommendation.GroupRecommendationCategorySelector.SelectedCategory;
import matchuri.backend.identity.member.entity.Member;
import matchuri.backend.identity.member.entity.MemberTasteProfile;
import matchuri.backend.catalog.entity.AttributeCategory;
import matchuri.backend.catalog.entity.Ingredient;
import matchuri.backend.catalog.entity.MenuAttributeCategory;
import matchuri.backend.catalog.entity.MenuItem;
import matchuri.backend.catalog.api.query.CatalogMenuIngredientQuery;
import matchuri.backend.catalog.api.query.CatalogMenuQuery;
import matchuri.backend.catalog.api.query.CatalogAttributeQuery;
import matchuri.backend.recommendation.algorithm.MenuRecommendationAlgorithm;
import matchuri.backend.recommendation.algorithm.MenuRecommendationAlgorithmResolver;
import matchuri.backend.recommendation.algorithm.RecommendationAlgorithmType;
import matchuri.backend.recommendation.algorithm.RecommendationTargetType;
import matchuri.backend.recommendation.algorithm.input.MenuRecommendationInput;
import matchuri.backend.recommendation.algorithm.input.MenuRecommendationProfile;
import matchuri.backend.recommendation.algorithm.input.RecommendationContextSnapshot;
import matchuri.backend.recommendation.algorithm.input.TasteProfileSnapshot;
import matchuri.backend.recommendation.algorithm.output.MenuRecommendationCandidateResult;
import matchuri.backend.recommendation.algorithm.output.MenuRecommendationResult;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroupRecommendationCandidateGenerator {

    private static final int GROUP_RECOMMENDATION_CANDIDATE_LIMIT = 3;

    private final GroupRoomMemberRepository groupRoomMemberRepository;
    private final CatalogMenuQuery menuItemRepository;
    private final CatalogMenuIngredientQuery menuIngredientRepository;
    private final GroupRecommendationCandidateRepository groupRecommendationCandidateRepository;
    private final GroupRecommendationCategoryRepository groupRecommendationCategoryRepository;
    private final CatalogAttributeQuery attributeCategoryRepository;
    private final GroupRecommendationCategorySelector categorySelector;
    private final MenuRecommendationAlgorithmResolver menuRecommendationAlgorithmResolver;
    private final GroupLocationManager groupLocationManager;
    private final ObjectMapper objectMapper;

    public List<GroupRecommendationCandidate> generateCandidatesForRecommendation(
            GroupRoom room,
            GroupRecommendation recommendation,
            String contextJson,
            List<Long> excludedMenuIds
    ) {
        List<GroupRoomMember> activeMembers = groupRoomMemberRepository.findActiveMembersByRoomId(room.getId());
        List<MenuItem> menuItems = menuItemRepository.searchActiveMenuItems(null, List.of(), true, List.of(), true);
        Map<Long, MenuItem> menuItemById = menuItems.stream()
                .collect(Collectors.toMap(MenuItem::getId, Function.identity()));
        String recommendationContextJson = contextJson == null
                ? groupLocationManager.toRecommendationContextJson(room)
                : contextJson;

        MenuRecommendationAlgorithm algorithm =
                menuRecommendationAlgorithmResolver.resolve(RecommendationAlgorithmType.GROUP);

        List<TasteProfileSnapshot> participants = toTasteProfileSnapshots(activeMembers);
        List<MenuRecommendationProfile> menuProfiles = toMenuRecommendationProfiles(menuItems);

        MenuRecommendationResult recommendationResult = algorithm.recommend(new MenuRecommendationInput(
                RecommendationTargetType.GROUP,
                participants,
                menuProfiles,
                RecommendationContextSnapshot.of(recommendationContextJson),
                GROUP_RECOMMENDATION_CANDIDATE_LIMIT,
                List.of(),
                excludedMenuIds,
                Map.of(),
                activeMembers.stream()
                        .map(GroupRoomMember::getMember)
                        .map(Member::getTasteProfile)
                        .filter(Objects::nonNull)
                        .flatMap(profile -> profile.getPreferAttributeCategories().stream())
                        .collect(Collectors.toMap(AttributeCategory::getId, AttributeCategory::getCategoryType,
                                (first, second) -> first))
        ));

        List<GroupRecommendationCandidate> candidates = saveGroupRecommendationCandidates(
                recommendation,
                recommendationResult,
                menuItemById
        );

        List<Long> candidateCategoryIds = recommendationResult.candidates().stream()
                .flatMap(candidate -> menuItemById.get(candidate.menuId()).getMenuAttributeCategories().stream())
                .map(MenuAttributeCategory::getAttributeCategory)
                .map(AttributeCategory::getId)
                .distinct()
                .toList();

        Map<Long, AttributeCategory> activeCategoriesById = attributeCategoryRepository
                .findAllByIdInAndActiveTrue(candidateCategoryIds).stream()
                .collect(Collectors.toMap(AttributeCategory::getId, Function.identity()));

        List<SelectedCategory> selectedCategories = categorySelector.select(
                participants,
                recommendationResult.candidates(),
                menuProfiles,
                activeCategoriesById
        );

        List<GroupRecommendationCategory> recommendationCategories = IntStream.range(0, selectedCategories.size())
                .mapToObj(index -> {
                    SelectedCategory selected = selectedCategories.get(index);
                    return new GroupRecommendationCategory(
                            recommendation,
                            activeCategoriesById.get(selected.categoryId()),
                            index + 1,
                            selected.source()
                    );
                })
                .toList();

        groupRecommendationCategoryRepository.saveAll(recommendationCategories);

        return candidates;
    }

    private List<TasteProfileSnapshot> toTasteProfileSnapshots(List<GroupRoomMember> activeMembers) {
        return activeMembers.stream()
                .map(GroupRoomMember::getMember)
                .map(member -> toTasteProfileSnapshot(member, member.getTasteProfile()))
                .toList();
    }

    private TasteProfileSnapshot toTasteProfileSnapshot(Member member, MemberTasteProfile tasteProfile) {
        if (tasteProfile == null) {
            return new TasteProfileSnapshot(
                    member.getId(),
                    String.valueOf(member.getId()),
                    List.of(),
                    List.of(),
                    List.of()
            );
        }

        return new TasteProfileSnapshot(
                member.getId(),
                String.valueOf(member.getId()),
                tasteProfile.getPreferAttributeCategories().stream()
                        .map(AttributeCategory::getId)
                        .toList(),
                tasteProfile.getRestrictionIngredients().stream()
                        .map(Ingredient::getId)
                        .toList(),
                tasteProfile.getDisLikeMenuItems().stream()
                        .map(MenuItem::getId)
                        .toList()
        );
    }

    private List<MenuRecommendationProfile> toMenuRecommendationProfiles(List<MenuItem> menuItems) {
        Map<Long, List<Long>> ingredientIdsByMenuId = menuIngredientRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        menuIngredient -> menuIngredient.getMenu().getId(),
                        Collectors.mapping(menuIngredient -> menuIngredient.getIngredient().getId(),
                                Collectors.toList())
                ));

        return menuItems.stream()
                .map(menuItem -> new MenuRecommendationProfile(
                        menuItem.getId(),
                        menuItem.getCode(),
                        menuItem.getName(),
                        menuItem.getMenuAttributeCategories().stream()
                                .map(MenuAttributeCategory::getAttributeCategory)
                                .map(AttributeCategory::getId)
                                .toList(),
                        ingredientIdsByMenuId.getOrDefault(menuItem.getId(), List.of())
                ))
                .toList();
    }

    private List<GroupRecommendationCandidate> saveGroupRecommendationCandidates(
            GroupRecommendation recommendation,
            MenuRecommendationResult recommendationResult,
            Map<Long, MenuItem> menuItemById
    ) {
        List<GroupRecommendationCandidate> candidates = recommendationResult.candidates().stream()
                .map(candidate -> new GroupRecommendationCandidate(
                        recommendation,
                        menuItemById.get(candidate.menuId()),
                        candidate.rankNo(),
                        candidate.score(),
                        toCandidateMetaJson(recommendationResult, candidate)
                ))
                .toList();

        return groupRecommendationCandidateRepository.saveAll(candidates);
    }

    private String toCandidateMetaJson(
            MenuRecommendationResult recommendationResult,
            MenuRecommendationCandidateResult candidate
    ) {
        Map<String, Object> meta = new LinkedHashMap<>();
        meta.put("algorithmType", recommendationResult.algorithmType().name());
        meta.put("algorithmVersion", recommendationResult.algorithmVersion());
        meta.put("scoreBreakdown", candidate.scoreBreakdown());
        meta.put("candidateMeta", candidate.meta());

        try {
            return objectMapper.writeValueAsString(meta);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("그룹 추천 후보 메타 정보를 JSON으로 변환할 수 없습니다.", exception);
        }
    }
}


