package matchuri.backend.catalog.query;

import java.util.List;
import matchuri.backend.catalog.entity.CategoryType;

public record GetAttributeCategoriesQuery(
        List<CategoryType> categoryTypes
) {
}
