package matchuri.backend.identity.member.service;

import matchuri.backend.identity.member.api.MemberAgreementService;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import matchuri.backend.identity.auth.result.IssuedAccessToken;
import matchuri.backend.identity.auth.support.token.JwtTokenProvider;
import matchuri.backend.identity.member.command.SubmitRequiredAgreementsCommand;
import matchuri.backend.identity.member.entity.AgreementType;
import matchuri.backend.identity.member.entity.Member;
import matchuri.backend.identity.member.entity.MemberAgreement;
import matchuri.backend.identity.member.repository.MemberAgreementRepository;
import matchuri.backend.identity.member.result.RequiredAgreementStatusResult;
import matchuri.backend.identity.member.result.SubmitRequiredAgreementsResult;
import matchuri.backend.identity.member.support.agreement.RequiredAgreementRequestValidator;
import matchuri.backend.identity.member.support.agreement.RequiredAgreementRevisionResolver;
import matchuri.backend.identity.member.support.agreement.RequiredAgreementVersions;
import matchuri.backend.identity.api.MemberReader;
import matchuri.backend.identity.member.support.onboarding.OnboardingStatusResolver;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberAgreementServiceImpl implements MemberAgreementService {

    private final MemberAgreementRepository memberAgreementRepository;
    private final RequiredAgreementRequestValidator requiredAgreementRequestValidator;
    private final JwtTokenProvider jwtTokenProvider;
    private final RequiredAgreementRevisionResolver requiredAgreementRevisionResolver;
    private final MemberReader memberReader;
    private final OnboardingStatusResolver onboardingStatusResolver;

    @Override
    public RequiredAgreementStatusResult getRequiredAgreementStatus(Long memberId) {
        Member member = memberReader.getActiveMember(memberId);
        return requiredAgreementRevisionResolver.calculateStatus(member.getId());
    }

    @Override
    @Transactional
    public SubmitRequiredAgreementsResult submitRequiredAgreements(
            Long memberId,
            SubmitRequiredAgreementsCommand command
    ) {
        Member member = memberReader.getActiveMember(memberId);

        Map<AgreementType, String> requestedVersions = requiredAgreementRequestValidator.validateAndIndex(
                command.agreements());
        for (AgreementType requiredType : RequiredAgreementVersions.requiredTypes()) {
            String requiredVersion = RequiredAgreementVersions.getRequiredVersion(requiredType);
            if (!memberAgreementRepository.existsByMemberIdAndAgreementTypeAndAgreementVersion(
                    member.getId(),
                    requiredType,
                    requiredVersion
            )) {
                memberAgreementRepository.save(
                        MemberAgreement.create(member, requiredType, requestedVersions.get(requiredType)));
            }
        }

        RequiredAgreementStatusResult status = requiredAgreementRevisionResolver.calculateStatus(member.getId());
        IssuedAccessToken issuedAccessToken = jwtTokenProvider.issueAccessToken(member,
                RequiredAgreementVersions.currentRevision());
        return new SubmitRequiredAgreementsResult(status, issuedAccessToken, onboardingStatusResolver.resolve(member));
    }

    @Override
    public boolean hasCompletedRequiredAgreements(Long memberId) {
        return requiredAgreementRevisionResolver.calculateStatus(memberId).requiredAgreementsCompleted();
    }

    @Override
    public String resolveRequiredAgreementRevision(Long memberId) {
        return requiredAgreementRevisionResolver.resolve(memberId);
    }
}
