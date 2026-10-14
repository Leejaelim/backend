package matchuri.backend.groupdecision.result;

import java.time.LocalDateTime;
import matchuri.backend.groupdecision.entity.GroupInviteStatus;
import matchuri.backend.groupdecision.entity.GroupMemberStatus;

public record RespondGroupInviteResult(
        Long inviteId,
        Long groupId,
        GroupInviteStatus inviteStatus,
        GroupMemberStatus memberStatus,
        LocalDateTime respondedAt
) {
}
