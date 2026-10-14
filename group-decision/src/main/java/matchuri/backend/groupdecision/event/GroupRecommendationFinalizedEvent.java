package matchuri.backend.groupdecision.event;

import java.time.LocalDateTime;
import matchuri.backend.groupdecision.entity.GroupRecommendationStatus;
import matchuri.backend.groupdecision.result.GroupRecommendationCandidateResult;

public record GroupRecommendationFinalizedEvent(
        Long groupId,
        Long sessionId,
        Long actorMemberId,
        GroupRecommendationStatus status,
        GroupRecommendationCandidateResult finalCandidate,
        LocalDateTime finalizedAt
) {
}
