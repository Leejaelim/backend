package matchuri.backend.identity.member.result;

import java.util.List;
import matchuri.backend.identity.member.entity.AgreementType;

public record RequiredAgreementStatusResult(
        boolean requiredAgreementsCompleted,
        List<AgreementType> missingAgreementTypes
) {
}
