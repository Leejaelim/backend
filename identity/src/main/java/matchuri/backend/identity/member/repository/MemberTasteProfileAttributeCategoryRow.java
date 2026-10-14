package matchuri.backend.identity.member.repository;

import matchuri.backend.catalog.entity.CategoryType;

public record MemberTasteProfileAttributeCategoryRow(
        Long id,
        CategoryType categoryType,
        String code,
        String name,
        Integer sortOrder
) {
}
