package matchuri.backend.realtime.result;

import java.time.LocalDateTime;

public record GroupDeletedRealtimePayload(
        Long groupId,
        Long deletedByMemberId,
        LocalDateTime deletedAt
) {
}
