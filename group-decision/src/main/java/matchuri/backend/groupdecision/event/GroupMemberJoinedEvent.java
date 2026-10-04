package matchuri.backend.groupdecision.event;

import java.time.LocalDateTime;

public record GroupMemberJoinedEvent(
        Long groupId,
        Long memberId,
        String memberNickname,
        LocalDateTime joinedAt
) {
}
