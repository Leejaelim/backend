package matchuri.backend.realtime.result;

import java.time.LocalDateTime;

public record GroupMemberJoinedRealtimePayload(
        Long groupId,
        Long memberId,
        String memberNickname,
        LocalDateTime joinedAt
) {
}
