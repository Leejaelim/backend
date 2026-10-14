package matchuri.backend.realtime.result;

import java.time.LocalDateTime;
import matchuri.backend.groupdecision.entity.GroupRecommendationStatus;

public record GroupRecommendationFinalizedRealtimePayload(
        Long sessionId,
        GroupRecommendationStatus status,
        LocalDateTime finalizedAt,
        RealtimeCandidatePayload finalCandidate
) {
}
