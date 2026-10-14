package matchuri.backend.groupdecision.repository;

import java.util.List;
import java.util.Optional;
import matchuri.backend.groupdecision.entity.GroupRoom;
import matchuri.backend.groupdecision.entity.GroupRoomStatus;

public interface GroupRoomRepositoryCustom {

    Optional<GroupRoom> findByIdAndStatusNotForUpdate(Long id, GroupRoomStatus status);

    List<GroupRoom> findOwnedNotDeletedForUpdate(Long memberId);
}
