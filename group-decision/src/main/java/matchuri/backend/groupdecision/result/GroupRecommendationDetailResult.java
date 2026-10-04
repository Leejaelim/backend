package matchuri.backend.groupdecision.result;

import java.util.List;

public record GroupRecommendationDetailResult(
        GroupRecommendationResult session,
        List<GroupRecommendationCategoryResult> recommendationCategories
) {
}
