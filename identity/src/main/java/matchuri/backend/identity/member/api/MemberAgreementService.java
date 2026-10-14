package matchuri.backend.identity.member.api;

import matchuri.backend.identity.member.command.SubmitRequiredAgreementsCommand;
import matchuri.backend.identity.member.result.RequiredAgreementStatusResult;
import matchuri.backend.identity.member.result.SubmitRequiredAgreementsResult;

public interface MemberAgreementService {

    RequiredAgreementStatusResult getRequiredAgreementStatus(Long memberId);

    SubmitRequiredAgreementsResult submitRequiredAgreements(Long memberId, SubmitRequiredAgreementsCommand command);

    boolean hasCompletedRequiredAgreements(Long memberId);

    String resolveRequiredAgreementRevision(Long memberId);
}
