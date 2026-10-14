package matchuri.backend.groupdecision.result;

import java.time.LocalDateTime;
import matchuri.backend.groupdecision.entity.GroupMemberRole;
import matchuri.backend.groupdecision.entity.GroupMemberStatus;

public record GroupMemberSummaryResult(
        Long memberId,
        String nickname,
        String memberProfileImageUrl,
        GroupMemberRole role,
        GroupMemberStatus status,
        LocalDateTime joinedAt,
        boolean isMe
) {
}
