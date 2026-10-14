package matchuri.backend.groupdecision.result;

import matchuri.backend.groupdecision.entity.GroupRecommendationCategory;
import matchuri.backend.groupdecision.entity.GroupRecommendationCategorySource;
import matchuri.backend.catalog.entity.CategoryType;

public record GroupRecommendationCategoryResult(
        Long id,
        CategoryType categoryType,
        String code,
        String name,
        int rankNo,
        GroupRecommendationCategorySource source
) {
    public static GroupRecommendationCategoryResult from(GroupRecommendationCategory category) {
        return new GroupRecommendationCategoryResult(
                category.getAttributeCategory().getId(),
                category.getAttributeCategory().getCategoryType(),
                category.getAttributeCategory().getCode(),
                category.getAttributeCategory().getName(),
                category.getRankNo(),
                category.getSource()
        );
    }
}
