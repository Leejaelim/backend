package matchuri.backend.recommendation.result;

import java.time.LocalDateTime;
import java.util.List;
import matchuri.backend.catalog.result.MenuAttributeCategoryResult;
import org.jspecify.annotations.Nullable;

public record PersonalRecommendationHomeResult(
        @Nullable PersonalRecommendationSummaryResult latestRecommendation,
        List<SelectedRecommendation> selectedRecommendations
) {
    public record SelectedRecommendation(
            Long id,
            LocalDateTime createdAt,
            String menuName,
            List<MenuAttributeCategoryResult> attributeCategories
    ) {
    }
}
