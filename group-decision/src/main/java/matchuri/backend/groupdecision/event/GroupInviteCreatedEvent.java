package matchuri.backend.groupdecision.event;

import java.time.LocalDateTime;

public record GroupInviteCreatedEvent(
        Long inviteId,
        Long groupId,
        String groupName,
        Long requestMemberId,
        String requestMemberNickname,
        Long targetMemberId,
        LocalDateTime expiresAt
) {
}
