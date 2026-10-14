package matchuri.backend.identity.member.support.agreement;

import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import matchuri.backend.identity.member.entity.AgreementType;
import matchuri.backend.identity.member.repository.MemberAgreementRepository;
import matchuri.backend.identity.member.result.RequiredAgreementStatusResult;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RequiredAgreementRevisionResolver {

    private final MemberAgreementRepository memberAgreementRepository;

    public RequiredAgreementStatusResult calculateStatus(Long memberId) {
        List<AgreementType> missingTypes = RequiredAgreementVersions.requiredTypes().stream()
                .filter(type -> !memberAgreementRepository.existsByMemberIdAndAgreementTypeAndAgreementVersion(
                        memberId,
                        type,
                        RequiredAgreementVersions.getRequiredVersion(type)
                ))
                .sorted(Comparator.comparing(Enum::name))
                .toList();

        return new RequiredAgreementStatusResult(missingTypes.isEmpty(), missingTypes);
    }

    public String resolve(Long memberId) {
        return calculateStatus(memberId).requiredAgreementsCompleted()
                ? RequiredAgreementVersions.currentRevision()
                : null;
    }
}
