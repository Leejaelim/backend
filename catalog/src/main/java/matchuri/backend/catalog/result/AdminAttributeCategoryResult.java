package matchuri.backend.catalog.result;

import matchuri.backend.catalog.entity.AttributeCategory;
import matchuri.backend.catalog.entity.CategoryType;

public record AdminAttributeCategoryResult(
        Long id,
        CategoryType categoryType,
        String code,
        String name,
        int sortOrder,
        boolean isActive
) {

    public static AdminAttributeCategoryResult from(AttributeCategory attributeCategory) {
        return new AdminAttributeCategoryResult(
                attributeCategory.getId(),
                attributeCategory.getCategoryType(),
                attributeCategory.getCode(),
                attributeCategory.getName(),
                attributeCategory.getSortOrder(),
                attributeCategory.isActive()
        );
    }
}
