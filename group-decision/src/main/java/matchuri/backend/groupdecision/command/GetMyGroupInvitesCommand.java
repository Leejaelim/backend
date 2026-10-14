package matchuri.backend.groupdecision.command;

import matchuri.backend.groupdecision.entity.GroupInviteStatus;

public record GetMyGroupInvitesCommand(
        GroupInviteStatus status,
        int page,
        int size
) {
}
