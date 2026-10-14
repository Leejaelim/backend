package matchuri.backend.groupdecision.result;

import java.time.LocalDateTime;
import matchuri.backend.groupdecision.entity.GroupInviteStatus;

public record GroupInviteSummaryResult(
        Long inviteId,
        Long groupId,
        String groupName,
        Long requestMemberId,
        String requestMemberNickname,
        GroupInviteStatus status,
        LocalDateTime expiresAt,
        LocalDateTime createdAt
) {
}
