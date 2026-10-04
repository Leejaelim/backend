package matchuri.backend.identity.member.repository;

import java.util.Optional;
import matchuri.backend.identity.member.entity.MemberTasteProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberTasteProfileRepository extends JpaRepository<MemberTasteProfile, Long> {

    Optional<MemberTasteProfile> findByMemberId(Long memberId);

    boolean existsByMemberId(Long memberId);
}
