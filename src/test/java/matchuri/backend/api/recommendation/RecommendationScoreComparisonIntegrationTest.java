package matchuri.backend.api.recommendation;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import matchuri.backend.domain.group.entity.GroupMemberRole;
import matchuri.backend.domain.group.entity.GroupRecommendation;
import matchuri.backend.domain.group.entity.GroupRoom;
import matchuri.backend.domain.group.support.recommendation.GroupRecommendationCandidateGenerator;
import matchuri.backend.domain.member.entity.Member;
import matchuri.backend.domain.member.entity.MemberTasteProfile;
import matchuri.backend.domain.member.entity.MemberTasteProfileCategory;
import matchuri.backend.domain.menu.entity.AttributeCategory;
import matchuri.backend.domain.menu.entity.CategoryType;
import matchuri.backend.domain.menu.entity.MenuAttributeCategory;
import matchuri.backend.domain.menu.entity.MenuItem;
import matchuri.backend.domain.recommendation.command.GuestPersonalRecommendationCommand;
import matchuri.backend.domain.recommendation.service.RecommendationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

/**
 * Keep this fixture and runner identical on the before/after branches.
 * Scores come from the checked-out branch's production services, not a reference formula.
 */
@SpringBootTest(properties =
        "spring.datasource.url=jdbc:h2:mem:recommendation_score_comparison;MODE=MySQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE")
@ActiveProfiles("test")
@Transactional
class RecommendationScoreComparisonIntegrationTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private GroupRecommendationCandidateGenerator groupCandidateGenerator;

    @Test
    @DisplayName("동일 입력의 개인·비회원·3인 그룹 실제 점수를 출력한다")
    void printActualScoresForIdenticalScenarios() {
        Map<String, AttributeCategory> categories = saveCategories();
        saveMenu("KOREAN_HOT", categories, "KOREAN", "HOT", "SPICY", "RICH", "SOUP", "CRISPY");
        saveMenu("CHINESE_COLD", categories, "CHINESE", "COLD", "FRESH", "GRILLED", "CHEWY");
        saveMenu("JAPANESE_HOT", categories, "JAPANESE", "HOT", "SWEET", "FRIED");

        List<Scenario> scenarios = List.of(
                new Scenario("EXCLUSIVE", List.of("KOREAN", "CHINESE", "HOT", "COLD")),
                new Scenario("MIXED", List.of("KOREAN", "CHINESE", "HOT", "COLD", "SPICY", "RICH")),
                new Scenario("OTHER_TYPES", List.of("SPICY", "RICH", "SOUP", "GRILLED", "CRISPY", "CHEWY"))
        );

        System.out.println("SCORE_COMPARISON conditions: 3 menus; no history/restrictions/dislikes; group=3 identical profiles");
        for (Scenario scenario : scenarios) {
            List<AttributeCategory> preferences = scenario.preferences().stream().map(categories::get).toList();
            Member owner = saveMemberWithPreferences(scenario.name() + "_owner", preferences);
            Member second = saveMemberWithPreferences(scenario.name() + "_second", preferences);
            Member third = saveMemberWithPreferences(scenario.name() + "_third", preferences);
            GroupRoom room = GroupRoom.createOwnedBy(scenario.name(), scenario.name(), owner);
            room.addGroupMember(second, GroupMemberRole.MEMBER);
            room.addGroupMember(third, GroupMemberRole.MEMBER);
            entityManager.persist(room);
            GroupRecommendation recommendation = GroupRecommendation.preparing(room);
            entityManager.persist(recommendation);
            Long ownerId = owner.getId();
            Long roomId = room.getId();
            Long recommendationId = recommendation.getId();
            List<Long> preferenceIds = preferences.stream().map(AttributeCategory::getId).toList();

            // Reload relationships from the database exactly as the real use cases do.
            entityManager.flush();
            entityManager.clear();

            List<ScoreRow> personal = recommendationService.createPersonalRecommendation(ownerId, "{}")
                    .candidates().stream()
                    .map(candidate -> new ScoreRow(candidate.rankNo(), candidate.menuName(), candidate.score()))
                    .toList();
            List<ScoreRow> guest = recommendationService.createGuestPersonalRecommendation(
                            new GuestPersonalRecommendationCommand(preferenceIds, List.of(), List.of(), "{}"))
                    .candidates().stream()
                    .map(candidate -> new ScoreRow(candidate.rankNo(), candidate.menuName(), candidate.score()))
                    .toList();
            List<ScoreRow> group = groupCandidateGenerator.generateCandidatesForRecommendation(
                            entityManager.find(GroupRoom.class, roomId),
                            entityManager.find(GroupRecommendation.class, recommendationId), "{}", List.of())
                    .stream()
                    .map(candidate -> new ScoreRow(candidate.getRankNo(), candidate.getMenuItem().getName(), candidate.getScore()))
                    .toList();

            System.out.println("SCORE_COMPARISON scenario=" + scenario.name() + " preferences=" + scenario.preferences());
            printAndValidate(scenario.name(), "PERSONAL", personal);
            printAndValidate(scenario.name(), "GUEST", guest);
            printAndValidate(scenario.name(), "GROUP", group);
            // With no history/dislikes and identical profiles, all three targets should agree.
            assertThat(personal).containsExactlyElementsOf(guest);
            assertThat(group).containsExactlyElementsOf(guest);
        }
    }

    private void printAndValidate(String scenario, String target, List<ScoreRow> rows) {
        assertThat(rows).hasSize(3);
        assertThat(rows).extracting(ScoreRow::menu)
                .containsExactlyInAnyOrder("KOREAN_HOT", "CHINESE_COLD", "JAPANESE_HOT");
        assertThat(rows).extracting(ScoreRow::rank).containsExactly(1, 2, 3);
        for (ScoreRow row : rows) {
            assertThat(row.score()).isBetween(0.0, 100.0);
            System.out.printf(Locale.ROOT,
                    "SCORE_COMPARISON scenario=%s target=%s rank=%d menu=%s score=%.1f%n",
                    scenario, target, row.rank(), row.menu(), row.score());
        }
    }

    private Map<String, AttributeCategory> saveCategories() {
        Map<String, AttributeCategory> categories = new LinkedHashMap<>();
        for (CategoryType type : CategoryType.values()) {
            List<String> codes = switch (type) {
                case FOOD_CATEGORY -> List.of("KOREAN", "CHINESE", "JAPANESE");
                case TEMPERATURE -> List.of("HOT", "COLD");
                case FLAVOR -> List.of("SPICY", "RICH", "FRESH", "SWEET");
                case COOKING_METHOD -> List.of("SOUP", "GRILLED", "FRIED");
                case TEXTURE -> List.of("CRISPY", "CHEWY");
            };
            for (String code : codes) {
                AttributeCategory category = new AttributeCategory(type, code, code, categories.size() + 1);
                entityManager.persist(category);
                categories.put(code, category);
            }
        }
        return categories;
    }

    private void saveMenu(String code, Map<String, AttributeCategory> categories, String... attributeCodes) {
        MenuItem menu = new MenuItem(code, code, "Score comparison fixture");
        entityManager.persist(menu);
        for (String attributeCode : attributeCodes) {
            entityManager.persist(new MenuAttributeCategory(menu, categories.get(attributeCode)));
        }
    }

    private Member saveMemberWithPreferences(String loginId, List<AttributeCategory> preferences) {
        Member member = Member.createWithEncodedPassword(loginId, "encoded-password", loginId, loginId + "@example.com");
        entityManager.persist(member);
        MemberTasteProfile profile = new MemberTasteProfile(member, "v1");
        entityManager.persist(profile);
        for (AttributeCategory category : preferences) {
            entityManager.persist(new MemberTasteProfileCategory(profile, category));
        }
        return member;
    }

    private record Scenario(String name, List<String> preferences) {
    }

    private record ScoreRow(int rank, String menu, double score) {
    }
}
