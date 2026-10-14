package matchuri.backend.groupdecision.command;

import matchuri.backend.groupdecision.entity.GroupRoomStatus;

public record GetMyGroupsCommand(
        GroupRoomStatus status,
        int page,
        int size
) {
}
