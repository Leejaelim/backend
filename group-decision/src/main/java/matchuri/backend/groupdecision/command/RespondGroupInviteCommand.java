package matchuri.backend.groupdecision.command;

import matchuri.backend.groupdecision.entity.GroupInviteResponseType;

public record RespondGroupInviteCommand(
        Long inviteId,
        GroupInviteResponseType responseType
) {
}
