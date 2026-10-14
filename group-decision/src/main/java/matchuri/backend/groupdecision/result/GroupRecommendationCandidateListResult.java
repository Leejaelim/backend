package matchuri.backend.groupdecision.result;

import java.util.List;

public record GroupRecommendationCandidateListResult(
        Long sessionId,
        List<GroupRecommendationCandidateResult> candidates
) {
}
