package matchuri.backend.identity.member.repository;

import matchuri.backend.identity.member.entity.AgreementType;
import matchuri.backend.identity.member.entity.MemberAgreement;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;

@NullMarked
public interface MemberAgreementRepository extends JpaRepository<MemberAgreement, Long> {

    boolean existsByMemberIdAndAgreementTypeAndAgreementVersion(Long memberId, AgreementType agreementType,
                                                                String agreementVersion);
}
