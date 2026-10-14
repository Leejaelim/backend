package matchuri.backend.application.api.memberagreement.mapper;

import matchuri.backend.application.api.common.dto.OnboardingStatusResponse;
import matchuri.backend.application.api.memberagreement.dto.request.SubmitRequiredAgreementsRequest;
import matchuri.backend.application.api.memberagreement.dto.response.RequiredAgreementStatusResponse;
import matchuri.backend.application.api.memberagreement.dto.response.SubmitRequiredAgreementsResponse;
import matchuri.backend.identity.member.command.SubmitRequiredAgreementsCommand;
import matchuri.backend.identity.member.result.RequiredAgreementStatusResult;
import matchuri.backend.identity.member.result.SubmitRequiredAgreementsResult;
import org.springframework.stereotype.Component;

@Component
public class MemberAgreementMapper {

    public SubmitRequiredAgreementsCommand toCommand(SubmitRequiredAgreementsRequest request) {
        return new SubmitRequiredAgreementsCommand(
                request.agreements().stream()
                        .map(agreement -> new SubmitRequiredAgreementsCommand.AgreementConsentCommand(
                                agreement.agreementType(),
                                agreement.agreementVersion()
                        ))
                        .toList()
        );
    }

    public RequiredAgreementStatusResponse toResponse(RequiredAgreementStatusResult result) {
        return new RequiredAgreementStatusResponse(
                result.requiredAgreementsCompleted(),
                result.missingAgreementTypes().stream()
                        .map(Enum::name)
                        .toList()
        );
    }

    public SubmitRequiredAgreementsResponse toResponse(SubmitRequiredAgreementsResult result) {
        return new SubmitRequiredAgreementsResponse(
                result.status().requiredAgreementsCompleted(),
                result.status().missingAgreementTypes().stream()
                        .map(Enum::name)
                        .toList(),
                new OnboardingStatusResponse(
                        result.onboarding().requiredAgreementsCompleted(),
                        result.onboarding().nicknameCompleted(),
                        result.onboarding().tasteProfileCompleted(),
                        result.onboarding().completed(),
                        result.onboarding().nextStep()
                ),
                result.issuedAccessToken().accessToken(),
                result.issuedAccessToken().expiresIn()
        );
    }
}
