package matchuri.backend.catalog.result;

import matchuri.backend.catalog.entity.CategoryType;

public record MenuAttributeCategoryResult(
        Long id, CategoryType categoryType, String code, String name, int sortOrder
) {
}
