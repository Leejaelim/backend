package matchuri.backend.identity.member.result;

import matchuri.backend.identity.auth.result.IssuedAccessToken;

public record SubmitRequiredAgreementsResult(
        RequiredAgreementStatusResult status,
        IssuedAccessToken issuedAccessToken,
        OnboardingStatusResult onboarding
) {
}
