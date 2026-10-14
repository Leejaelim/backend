package matchuri.backend.realtime.result;

import java.time.LocalDateTime;
import matchuri.backend.realtime.entity.RealtimeEventType;

public record RealtimeEventEnvelope(
        String eventId,
        RealtimeEventType eventType,
        LocalDateTime occurredAt,
        Long groupId,
        Long sessionId,
        Long actorMemberId,
        Object payload
) {
}
