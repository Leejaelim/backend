package matchuri.backend.recommendation.algorithm.input;

public record RecommendationContextSnapshot(
        String contextJson
) {
    public static RecommendationContextSnapshot of(String contextJson) {
        return new RecommendationContextSnapshot(contextJson == null ? "{}" : contextJson);
    }
}
