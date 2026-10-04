package matchuri.backend.groupdecision.result;

import java.time.LocalDateTime;
import matchuri.backend.groupdecision.entity.GroupRoomStatus;

public record DeleteGroupResult(
        Long groupId,
        GroupRoomStatus status,
        LocalDateTime deletedAt
) {
}
