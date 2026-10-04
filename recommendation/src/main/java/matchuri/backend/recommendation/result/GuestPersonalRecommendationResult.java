package matchuri.backend.recommendation.result;

import java.util.List;

public record GuestPersonalRecommendationResult(
        List<GuestPersonalRecommendationCandidateResult> candidates
) {
    public GuestPersonalRecommendationResult {
        candidates = candidates == null ? List.of() : List.copyOf(candidates);
    }
}
