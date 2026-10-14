package matchuri.backend.realtime.result;

import java.util.List;
import matchuri.backend.groupdecision.entity.GroupRecommendationStatus;

public record GroupRecommendationOpenedRealtimePayload(
        Long sessionId,
        GroupRecommendationStatus status,
        List<RealtimeCandidatePayload> candidates,
        RealtimeVoteProgressPayload voteProgress
) {
}
