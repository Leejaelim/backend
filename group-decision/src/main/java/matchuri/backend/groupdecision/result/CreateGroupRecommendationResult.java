package matchuri.backend.groupdecision.result;

import java.util.List;
import matchuri.backend.groupdecision.entity.GroupRecommendationStatus;

public record CreateGroupRecommendationResult(
        Long sessionId,
        GroupRecommendationStatus status,
        List<GroupRecommendationCandidateResult> candidates
) {
}
