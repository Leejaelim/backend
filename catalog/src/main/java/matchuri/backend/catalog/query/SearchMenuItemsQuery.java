package matchuri.backend.catalog.query;

import java.util.List;

public record SearchMenuItemsQuery(
        String query,
        List<Long> attributeCategoryIds,
        List<Long> ingredientIds
) {
}
