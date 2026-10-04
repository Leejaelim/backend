package matchuri.backend.realtime.result;

import matchuri.backend.groupdecision.entity.GroupRecommendationStatus;

public record GroupRecommendationStartedRealtimePayload(
        Long sessionId,
        GroupRecommendationStatus status,
        RealtimeReadinessProgressPayload readinessProgress
) {
}
