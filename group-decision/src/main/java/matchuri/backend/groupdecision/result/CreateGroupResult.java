package matchuri.backend.groupdecision.result;

import matchuri.backend.groupdecision.entity.GroupRoomStatus;

public record CreateGroupResult(
        Long groupId,
        String inviteCode,
        GroupRoomStatus status
) {
}
