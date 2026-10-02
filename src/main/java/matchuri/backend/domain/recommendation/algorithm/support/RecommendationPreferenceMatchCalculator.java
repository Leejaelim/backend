package matchuri.backend.domain.recommendation.algorithm.support;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import matchuri.backend.domain.menu.entity.CategoryType;

public final class RecommendationPreferenceMatchCalculator {

    private RecommendationPreferenceMatchCalculator() {
    }

    public static PreferenceMatch calculate(
            List<Long> menuAttributeIds,
            List<Long> preferredAttributeIds,
            Map<Long, CategoryType> categoryTypes
    ) {
        Set<Long> preferredIds = new HashSet<>(preferredAttributeIds);
        Set<Long> matchingIds = new HashSet<>(menuAttributeIds);
        matchingIds.retainAll(preferredIds);

        return new PreferenceMatch(countUnits(matchingIds, categoryTypes), countUnits(preferredIds, categoryTypes));
    }

    private static int countUnits(Set<Long> categoryIds, Map<Long, CategoryType> categoryTypes) {
        boolean foodCategoryPresent = false;
        boolean temperaturePresent = false;
        int individualCount = 0;

        for (Long categoryId : categoryIds) {
            CategoryType type = categoryTypes.get(categoryId);
            if (type == CategoryType.FOOD_CATEGORY) {
                foodCategoryPresent = true;
            } else if (type == CategoryType.TEMPERATURE) {
                temperaturePresent = true;
            } else {
                individualCount++;
            }
        }

        return individualCount + (foodCategoryPresent ? 1 : 0) + (temperaturePresent ? 1 : 0);
    }

    public record PreferenceMatch(long matchingCount, int preferredUnitCount) {
    }
}
