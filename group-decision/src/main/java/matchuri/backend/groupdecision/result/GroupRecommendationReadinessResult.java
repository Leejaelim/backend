package matchuri.backend.groupdecision.result;

import java.util.List;
import matchuri.backend.groupdecision.entity.GroupRecommendationStatus;

public record GroupRecommendationReadinessResult(
        Long sessionId,
        GroupRecommendationStatus status,
        GroupRecommendationReadinessProgressResult progress,
        List<GroupRecommendationReadinessMemberResult> members
) {
}
