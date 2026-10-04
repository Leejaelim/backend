package matchuri.backend.identity.member.repository;

import java.util.Optional;
import matchuri.backend.identity.member.entity.MemberLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberLocationRepository extends JpaRepository<MemberLocation, Long> {

    Optional<MemberLocation> findByMemberId(Long memberId);
}
