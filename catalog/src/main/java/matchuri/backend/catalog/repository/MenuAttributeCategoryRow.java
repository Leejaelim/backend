package matchuri.backend.catalog.repository;

import matchuri.backend.catalog.entity.CategoryType;

public record MenuAttributeCategoryRow(
        Long id,
        CategoryType categoryType,
        String code,
        String name,
        Integer sortOrder
) {
}
