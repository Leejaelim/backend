package matchuri.backend.catalog.result;

import java.util.List;

public record MenuItemDetailResult(
        Long id,
        String code,
        String name,
        String description,
        String thumbnailUrl,
        List<AttributeCategoryResult> attributeCategories,
        List<RestrictionIngredientResult> ingredients
) {
}
