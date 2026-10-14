package matchuri.backend.realtime.result;

import matchuri.backend.groupdecision.entity.GroupRecommendationStatus;

public record GroupRecommendationReadinessUpdatedRealtimePayload(
        Long sessionId,
        GroupRecommendationStatus status,
        Long readyMemberId,
        String readyMemberNickname,
        RealtimeReadinessProgressPayload readinessProgress
) {
}
