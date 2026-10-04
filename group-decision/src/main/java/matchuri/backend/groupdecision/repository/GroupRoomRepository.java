package matchuri.backend.groupdecision.repository;

import java.util.List;
import java.util.Optional;
import matchuri.backend.groupdecision.entity.GroupRoom;
import matchuri.backend.groupdecision.entity.GroupRoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRoomRepository extends JpaRepository<GroupRoom, Long>, GroupRoomRepositoryCustom {

    Optional<GroupRoom> findByIdAndStatusNot(Long id, GroupRoomStatus status);

    boolean existsByInviteCode(String inviteCode);

    Optional<GroupRoom> findByInviteCode(String inviteCode);
}
