package matchuri.backend.groupdecision.result;

import java.time.LocalDateTime;
import matchuri.backend.groupdecision.entity.GroupRecommendationStatus;

public record FinalizeGroupRecommendationResult(
        Long sessionId,
        GroupRecommendationStatus status,
        GroupRecommendationCandidateResult finalCandidate,
        LocalDateTime finalizedAt
) {
}
