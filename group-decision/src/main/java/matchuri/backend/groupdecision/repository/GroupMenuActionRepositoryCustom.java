package matchuri.backend.groupdecision.repository;

import java.time.LocalDateTime;
import java.util.List;
import matchuri.backend.groupdecision.entity.GroupMenuActionType;

public interface GroupMenuActionRepositoryCustom {

    List<Long> findMenuItemIdsByGroupRoomIdAndActionTypeAndCreatedAtAfter(Long groupRoomId, GroupMenuActionType actionType, LocalDateTime createdAt);
}
