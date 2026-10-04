package matchuri.backend.catalog.result;

import matchuri.backend.catalog.entity.AttributeCategory;
import matchuri.backend.catalog.entity.CategoryType;

public record AttributeCategoryResult(
        Long id,
        CategoryType categoryType,
        String code,
        String name,
        int sortOrder
) {

    public static AttributeCategoryResult from(AttributeCategory attributeCategory) {
        return new AttributeCategoryResult(
                attributeCategory.getId(),
                attributeCategory.getCategoryType(),
                attributeCategory.getCode(),
                attributeCategory.getName(),
                attributeCategory.getSortOrder()
        );
    }
}
