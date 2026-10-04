package matchuri.backend.groupdecision.event;

import matchuri.backend.groupdecision.entity.GroupRecommendationStatus;
import matchuri.backend.groupdecision.result.GroupRecommendationReadinessProgressResult;

public record GroupRecommendationReadinessUpdatedEvent(
        Long groupId,
        Long sessionId,
        Long readyMemberId,
        String readyMemberNickname,
        GroupRecommendationStatus status,
        GroupRecommendationReadinessProgressResult readinessProgress
) {
}
