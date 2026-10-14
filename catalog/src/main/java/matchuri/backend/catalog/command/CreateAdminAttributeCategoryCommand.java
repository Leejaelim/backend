package matchuri.backend.catalog.command;

import matchuri.backend.catalog.entity.CategoryType;

public record CreateAdminAttributeCategoryCommand(
        CategoryType categoryType,
        String code,
        String name,
        int sortOrder
) {
}
