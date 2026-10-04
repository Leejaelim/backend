package matchuri.backend.catalog.result;

import java.util.List;

public record RecommendationMenuResult(
        Long menuId,
        String menuCode,
        String menuName,
        List<Long> attributeCategoryIds,
        List<Long> ingredientIds
) {
    public RecommendationMenuResult {
        attributeCategoryIds = List.copyOf(attributeCategoryIds);
        ingredientIds = List.copyOf(ingredientIds);
    }
}
