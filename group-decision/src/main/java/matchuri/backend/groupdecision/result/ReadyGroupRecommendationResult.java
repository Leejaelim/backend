package matchuri.backend.groupdecision.result;

import java.util.List;
import matchuri.backend.groupdecision.entity.GroupRecommendationStatus;

public record ReadyGroupRecommendationResult(
        Long sessionId,
        GroupRecommendationStatus status,
        GroupRecommendationReadinessProgressResult readiness,
        List<GroupRecommendationCandidateResult> candidates
) {
}
