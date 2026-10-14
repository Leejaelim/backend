package matchuri.backend.groupdecision.event;

import matchuri.backend.groupdecision.entity.GroupRecommendationStatus;
import matchuri.backend.groupdecision.result.GroupRecommendationReadinessProgressResult;

public record GroupRecommendationStartedEvent(
        Long groupId,
        Long sessionId,
        Long actorMemberId,
        GroupRecommendationStatus status,
        GroupRecommendationReadinessProgressResult readinessProgress
) {
}
