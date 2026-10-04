package matchuri.backend.groupdecision.result;

import java.time.LocalDateTime;
import matchuri.backend.groupdecision.entity.GroupInviteStatus;

public record CreateNicknameGroupInviteResult(
        Long inviteId,
        Long groupId,
        String groupName,
        Long targetMemberId,
        String targetNickname,
        LocalDateTime expiresAt,
        GroupInviteStatus status
) {
}
