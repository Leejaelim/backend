package matchuri.backend.domain.member.service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import matchuri.backend.domain.auth.support.verification.EmailVerificationTokenVerifier;
import matchuri.backend.domain.member.command.CreateMemberCommand;
import matchuri.backend.domain.member.command.PutMemberLocationCommand;
import matchuri.backend.domain.member.command.RegisterLocalMemberCommand;
import matchuri.backend.domain.member.command.UpdateMemberBasicInfoCommand;
import matchuri.backend.domain.member.command.UpdateMemberPasswordCommand;
import matchuri.backend.domain.member.command.UpdateMemberTasteProfileCommand;
import matchuri.backend.domain.member.entity.Member;
import matchuri.backend.domain.member.entity.MemberAgreement;
import matchuri.backend.domain.member.entity.MemberLocation;
import matchuri.backend.domain.member.entity.MemberStatus;
import matchuri.backend.domain.member.entity.MemberTasteProfile;
import matchuri.backend.domain.member.entity.MemberTasteProfileCategory;
import matchuri.backend.domain.member.entity.MemberTasteProfileDislikedMenuItem;
import matchuri.backend.domain.member.entity.MemberTasteProfileRestrictionIngredient;
import matchuri.backend.domain.member.exception.MemberErrorCode;
import matchuri.backend.domain.member.repository.MemberAgreementRepository;
import matchuri.backend.domain.member.repository.MemberLocationRepository;
import matchuri.backend.domain.member.repository.MemberRepository;
import matchuri.backend.domain.member.repository.MemberTasteProfileCategoryRepository;
import matchuri.backend.domain.member.repository.MemberTasteProfileDislikedMenuItemRepository;
import matchuri.backend.domain.member.repository.MemberTasteProfileRepository;
import matchuri.backend.domain.member.repository.MemberTasteProfileRestrictionIngredientRepository;
import matchuri.backend.domain.member.result.CreateMemberResult;
import matchuri.backend.domain.member.result.MemberProfileResult;
import matchuri.backend.domain.member.result.MemberLocationResult;
import matchuri.backend.domain.member.result.MemberTasteProfileSummaryResult;
import matchuri.backend.domain.member.result.MemberTasteUpdateResult;
import matchuri.backend.domain.member.result.RegisterLocalMemberResult;
import matchuri.backend.domain.member.result.UpdateMemberPasswordResult;
import matchuri.backend.domain.member.result.UpdateMemberResult;
import matchuri.backend.domain.member.result.WithdrawMemberResult;
import matchuri.backend.domain.member.support.agreement.RequiredAgreementRequestValidator;
import matchuri.backend.domain.member.support.member.MemberReader;
import matchuri.backend.domain.member.support.onboarding.OnboardingStatusResolver;
import matchuri.backend.domain.menu.entity.AttributeCategory;
import matchuri.backend.domain.menu.entity.Ingredient;
import matchuri.backend.domain.menu.entity.MenuItem;
import matchuri.backend.domain.menu.repository.AttributeCategoryRepository;
import matchuri.backend.domain.menu.repository.IngredientRepository;
import matchuri.backend.domain.menu.repository.MenuItemRepository;
import matchuri.backend.domain.recommendation.entity.PersonalRecommendationStatus;
import matchuri.backend.domain.recommendation.repository.PersonalRecommendationRepository;
import matchuri.backend.global.exception.BusinessException;
import matchuri.backend.global.exception.RequestValidationException;
import org.jspecify.annotations.Nullable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberAgreementRepository memberAgreementRepository;
    private final MemberLocationRepository memberLocationRepository;
    private final MemberTasteProfileRepository memberTasteProfileRepository;
    private final MemberTasteProfileCategoryRepository memberTasteProfileCategoryRepository;
    private final MemberTasteProfileRestrictionIngredientRepository memberTasteProfileRestrictionIngredientRepository;
    private final MemberTasteProfileDislikedMenuItemRepository memberTasteProfileDislikedMenuItemRepository;
    private final AttributeCategoryRepository attributeCategoryRepository;
    private final IngredientRepository ingredientRepository;
    private final MenuItemRepository menuItemRepository;
    private final RequiredAgreementRequestValidator requiredAgreementRequestValidator;
    private final PasswordEncoder passwordEncoder;
    private final MemberReader memberReader;
    private final OnboardingStatusResolver onboardingStatusResolver;
    private final EmailVerificationTokenVerifier emailVerificationTokenVerifier;
    private final PersonalRecommendationRepository personalRecommendationRepository;

    @Override
    public boolean existsByLoginId(String loginId) {
        validateLoginId(loginId);
        return memberRepository.existsByLoginId(loginId);
    }

    private void validateLoginId(String loginId) {
        if (loginId == null || loginId.isBlank()) {
            throw RequestValidationException.invalidPathVariable("loginId", "로그인 아이디는 비어 있을 수 없습니다.");
        }

        if (loginId.length() > Member.LOGIN_ID_MAX_SIZE) {
            throw RequestValidationException.invalidPathVariable(
                    "loginId",
                    "로그인 아이디는 " + Member.LOGIN_ID_MAX_SIZE + "자를 초과할 수 없습니다."
            );
        }

        if (!loginId.matches(Member.LOGIN_ID_PATTERN)) {
            throw RequestValidationException.invalidPathVariable(
                    "loginId",
                    "로그인 아이디는 영문, 숫자, 점(.), 밑줄(_), 하이픈(-)만 사용할 수 있습니다."
            );
        }
    }

    @Override
    public boolean existsByNickname(String nickname) {
        validateNickname(nickname);
        return memberRepository.existsByNickname(nickname);
    }

    private void validateNickname(String nickname) {
        if (nickname == null || nickname.isBlank()) {
            throw RequestValidationException.invalidPathVariable("nickname", "닉네임은 비어 있을 수 없습니다.");
        }

        if (nickname.length() > Member.NICKNAME_MAX_SIZE) {
            throw RequestValidationException.invalidPathVariable(
                    "nickname",
                    "닉네임은 " + Member.NICKNAME_MAX_SIZE + "자를 초과할 수 없습니다."
            );
        }
    }

    @Override
    @Transactional
    public RegisterLocalMemberResult registerLocalMember(RegisterLocalMemberCommand command) {
        String loginId = command.loginId();

        emailVerificationTokenVerifier.verifySignupToken(command.email(), command.emailVerificationToken());
        validateEmailDuplication(command.email());

        String passwordHash = passwordEncoder.encode(command.password());
        Member member = createLocalMember(loginId, passwordHash, command.nickname(), command.email());

        requiredAgreementRequestValidator.validateAndIndex(command.agreements())
                .forEach((agreementType, agreementVersion) ->
                        memberAgreementRepository.save(MemberAgreement.create(member, agreementType, agreementVersion))
                );

        return RegisterLocalMemberResult.from(member);
    }

    @Override
    @Transactional
    public CreateMemberResult createMember(CreateMemberCommand command) {
        String loginId = command.loginId();
        if (memberRepository.existsByLoginId(loginId)) {
            throw new BusinessException(MemberErrorCode.DUPLICATE_LOGIN_ID, loginId);
        }

        String passwordHash = passwordEncoder.encode(command.password());
        Member member = createLocalMember(loginId, passwordHash, null, null);

        return CreateMemberResult.from(member);
    }

    @Override
    public MemberProfileResult getMyProfile(Long memberId) {
        Member member = memberReader.getActiveMember(memberId);

        return MemberProfileResult.from(member);
    }

    @Override
    public @Nullable MemberLocationResult getMyLocation(Long memberId) {
        Member member = memberReader.getActiveMember(memberId);
        MemberLocation location = memberLocationRepository.findByMemberId(member.getId()).orElse(null);

        return location == null ? null : MemberLocationResult.from(location);
    }

    @Override
    @Transactional
    public MemberLocationResult putMyLocation(Long memberId, PutMemberLocationCommand command) {
        Member member = memberReader.getActiveMember(memberId);
        MemberLocation location = memberLocationRepository.findByMemberId(member.getId()).orElse(null);

        if (location == null) {
            location = memberLocationRepository.save(new MemberLocation(
                    member,
                    command.latitude(),
                    command.longitude(),
                    command.radiusMeters(),
                    command.address()
            ));
        } else {
            location.update(command.latitude(), command.longitude(), command.radiusMeters(), command.address());
        }

        return MemberLocationResult.from(location);
    }

    @Override
    @Transactional
    public MemberTasteProfileSummaryResult getMyTasteProfile(Long memberId) {
        Member member = memberReader.getActiveMember(memberId);

        return memberTasteProfileRepository.findByMemberId(member.getId())
                .map(tasteProfile -> MemberTasteProfileSummaryResult.of(
                        member.getId(),
                        tasteProfile,
                        memberTasteProfileCategoryRepository.findAllByProfileIdOrderByDisplay(tasteProfile.getId()),
                        memberTasteProfileRestrictionIngredientRepository.findAllByProfileIdOrderByDisplay(
                                tasteProfile.getId()),
                        memberTasteProfileDislikedMenuItemRepository.findAllByProfileIdOrderByDisplay(
                                tasteProfile.getId())
                ))
                .orElseGet(() -> MemberTasteProfileSummaryResult.empty(member.getId()));
    }

    @Override
    @Transactional
    public UpdateMemberResult updateMyProfile(Long memberId, UpdateMemberBasicInfoCommand command) {
        Member member = memberReader.getActiveMember(memberId);

        if (command.nickname() != null) {
            String nickname = command.nickname().isBlank() ? null : command.nickname();
            validateNicknameDuplication(member, nickname);

            try {
                member.updateNickname(nickname);
                memberRepository.flush();
            } catch (DataIntegrityViolationException exception) {
                throw new BusinessException(MemberErrorCode.DUPLICATE_NICKNAME, nickname);
            }
        }

        return UpdateMemberResult.from(member, onboardingStatusResolver.resolve(member));
    }

    @Override
    @Transactional
    public UpdateMemberPasswordResult updateMyPassword(Long memberId, UpdateMemberPasswordCommand command) {
        Member member = memberReader.getActiveMember(memberId);

        if (member.getPasswordHash() == null
                || !passwordEncoder.matches(command.currentPassword(), member.getPasswordHash())) {
            throw new BusinessException(MemberErrorCode.INVALID_PASSWORD);
        }

        member.updatePasswordHash(passwordEncoder.encode(command.newPassword()));
        return UpdateMemberPasswordResult.success();
    }

    @Override
    @Transactional
    public MemberTasteUpdateResult updateMyTasteProfile(Long memberId, UpdateMemberTasteProfileCommand command) {
        Member member = memberReader.getActiveMember(memberId);
        List<Long> attributeCategoryIds = command.attributeCategoryIds();
        List<Long> restrictionIngredientIds = command.restrictionIngredientIds();
        List<Long> dislikedMenuItemIds = command.dislikedMenuItemIds();

        validateNoDuplicateIds(attributeCategoryIds, MemberErrorCode.DUPLICATE_TASTE_ATTRIBUTE_CATEGORY);
        validateNoDuplicateIds(restrictionIngredientIds, MemberErrorCode.DUPLICATE_TASTE_RESTRICTION_INGREDIENT);
        validateNoDuplicateIds(dislikedMenuItemIds, MemberErrorCode.DUPLICATE_TASTE_DISLIKED_MENU_ITEM);

        Map<Long, AttributeCategory> attributeCategoriesById = loadActiveAttributeCategories(attributeCategoryIds);
        Map<Long, Ingredient> ingredientsById = loadActiveIngredients(restrictionIngredientIds);
        Map<Long, MenuItem> menuItemsById = loadActiveMenuItems(dislikedMenuItemIds);

        MemberTasteProfile tasteProfile = memberTasteProfileRepository.findByMemberId(member.getId())
                .orElseGet(() -> memberTasteProfileRepository.saveAndFlush(
                        new MemberTasteProfile(member, MemberTasteProfileSummaryResult.DEFAULT_PROFILE_VERSION)
                ));

        replaceAttributeCategoryMappings(tasteProfile, attributeCategoryIds, attributeCategoriesById);
        replaceRestrictionIngredientMappings(tasteProfile, restrictionIngredientIds, ingredientsById);
        replaceDislikedMenuItemMappings(tasteProfile, dislikedMenuItemIds, menuItemsById);

        Long openPersonalRecommendationId =
                personalRecommendationRepository
                        .findFirstByMemberIdAndStatusAndSelectedCandidateIsNullAndClosedAtIsNullOrderByRequestedAtDescIdDesc(
                                member.getId(),
                                PersonalRecommendationStatus.OPEN
                        )
                        .map(recommendation -> recommendation.getId())
                        .orElse(null);

        MemberTasteProfileSummaryResult memberTasteProfileSummaryResult = MemberTasteProfileSummaryResult.of(
                member.getId(),
                tasteProfile,
                memberTasteProfileCategoryRepository.findAllByProfileIdOrderByDisplay(tasteProfile.getId()),
                memberTasteProfileRestrictionIngredientRepository.findAllByProfileIdOrderByDisplay(
                        tasteProfile.getId()),
                memberTasteProfileDislikedMenuItemRepository.findAllByProfileIdOrderByDisplay(tasteProfile.getId())
        );

        return new MemberTasteUpdateResult(memberTasteProfileSummaryResult, openPersonalRecommendationId);
    }

    @Override
    @Transactional
    public WithdrawMemberResult withdraw(Long memberId) {
        Member member = memberReader.getActiveMember(memberId);
        member.withdraw();

        return WithdrawMemberResult.from(member);
    }

    private Member createLocalMember(String loginId, String passwordHash, String nickname, String email) {
        validateLoginIdDuplication(null, loginId);
        validateNicknameDuplication(null, nickname);

        try {
            Member newMember = Member.createWithEncodedPassword(loginId, passwordHash, nickname, email);
            return memberRepository.saveAndFlush(newMember);
        } catch (DataIntegrityViolationException exception) {
            if (nickname != null && memberRepository.existsByNickname(nickname)) {
                throw new BusinessException(MemberErrorCode.DUPLICATE_NICKNAME, nickname);
            }
            throw new BusinessException(MemberErrorCode.DUPLICATE_LOGIN_ID, loginId);
        }
    }

    private void validateEmailDuplication(String email) {
        if (memberRepository.existsByEmailAndSocialFalseAndStatus(email, MemberStatus.ACTIVE)) {
            throw new BusinessException(MemberErrorCode.DUPLICATE_EMAIL, email);
        }
    }

    private void validateLoginIdDuplication(Member member, String loginId) {
        if (loginId == null || (member != null && loginId.equals(member.getLoginId()))) {
            return;
        }

        if (memberRepository.existsByLoginId(loginId)) {
            throw new BusinessException(MemberErrorCode.DUPLICATE_LOGIN_ID, loginId);
        }
    }

    private void validateNicknameDuplication(Member member, String nickname) {
        if (nickname == null || (member != null && nickname.equals(member.getNickname()))) {
            return;
        }

        if (memberRepository.existsByNickname(nickname)) {
            throw new BusinessException(MemberErrorCode.DUPLICATE_NICKNAME, nickname);
        }
    }

    private void validateNoDuplicateIds(List<Long> ids, MemberErrorCode errorCode) {
        if (ids.size() == new LinkedHashSet<>(ids).size()) {
            return;
        }

        throw new BusinessException(errorCode, ids);
    }

    private Map<Long, AttributeCategory> loadActiveAttributeCategories(List<Long> attributeCategoryIds) {
        List<AttributeCategory> attributeCategories = attributeCategoryRepository.findAllByIdInAndActiveTrue(
                attributeCategoryIds);
        if (attributeCategories.size() != attributeCategoryIds.size()) {
            throw new BusinessException(MemberErrorCode.INVALID_TASTE_ATTRIBUTE_CATEGORY, attributeCategoryIds);
        }

        return attributeCategories.stream()
                .collect(Collectors.toMap(AttributeCategory::getId, Function.identity()));
    }

    private Map<Long, Ingredient> loadActiveIngredients(List<Long> restrictionIngredientIds) {
        List<Ingredient> ingredients = ingredientRepository.findAllByIdInAndActiveTrue(restrictionIngredientIds);
        if (ingredients.size() != restrictionIngredientIds.size()) {
            throw new BusinessException(MemberErrorCode.INVALID_TASTE_RESTRICTION_INGREDIENT, restrictionIngredientIds);
        }

        return ingredients.stream()
                .collect(Collectors.toMap(Ingredient::getId, Function.identity()));
    }

    private Map<Long, MenuItem> loadActiveMenuItems(List<Long> dislikedMenuItemIds) {
        List<MenuItem> menuItems = menuItemRepository.findAllByIdInAndActiveTrue(dislikedMenuItemIds);
        if (menuItems.size() != dislikedMenuItemIds.size()) {
            throw new BusinessException(MemberErrorCode.INVALID_TASTE_DISLIKED_MENU_ITEM, dislikedMenuItemIds);
        }

        return menuItems.stream()
                .collect(Collectors.toMap(MenuItem::getId, Function.identity()));
    }

    private void replaceAttributeCategoryMappings(
            MemberTasteProfile tasteProfile,
            List<Long> attributeCategoryIds,
            Map<Long, AttributeCategory> attributeCategoriesById
    ) {
        memberTasteProfileCategoryRepository.deleteAllInBatch(
                memberTasteProfileCategoryRepository.findAllByProfileId(tasteProfile.getId())
        );

        if (attributeCategoryIds.isEmpty()) {
            return;
        }

        memberTasteProfileCategoryRepository.saveAll(
                attributeCategoryIds.stream()
                        .map(attributeCategoryId -> new MemberTasteProfileCategory(
                                tasteProfile,
                                attributeCategoriesById.get(attributeCategoryId)
                        ))
                        .toList()
        );
    }

    private void replaceRestrictionIngredientMappings(
            MemberTasteProfile tasteProfile,
            List<Long> restrictionIngredientIds,
            Map<Long, Ingredient> ingredientsById
    ) {
        memberTasteProfileRestrictionIngredientRepository.deleteAllInBatch(
                memberTasteProfileRestrictionIngredientRepository.findAllByProfileId(tasteProfile.getId())
        );

        if (restrictionIngredientIds.isEmpty()) {
            return;
        }

        memberTasteProfileRestrictionIngredientRepository.saveAll(
                restrictionIngredientIds.stream()
                        .map(restrictionIngredientId -> new MemberTasteProfileRestrictionIngredient(
                                tasteProfile,
                                ingredientsById.get(restrictionIngredientId)
                        ))
                        .toList()
        );
    }

    private void replaceDislikedMenuItemMappings(
            MemberTasteProfile tasteProfile,
            List<Long> dislikedMenuItemIds,
            Map<Long, MenuItem> menuItemsById
    ) {
        memberTasteProfileDislikedMenuItemRepository.deleteAllInBatch(
                memberTasteProfileDislikedMenuItemRepository.findAllByProfileId(tasteProfile.getId())
        );

        if (dislikedMenuItemIds.isEmpty()) {
            return;
        }

        memberTasteProfileDislikedMenuItemRepository.saveAll(
                dislikedMenuItemIds.stream()
                        .map(dislikedMenuItemId -> new MemberTasteProfileDislikedMenuItem(
                                tasteProfile,
                                menuItemsById.get(dislikedMenuItemId)
                        ))
                        .toList()
        );
    }
}
