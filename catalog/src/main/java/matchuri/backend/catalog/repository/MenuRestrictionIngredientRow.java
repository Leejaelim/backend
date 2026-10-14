package matchuri.backend.catalog.repository;

public record MenuRestrictionIngredientRow(
        Long id,
        String code,
        String name,
        Boolean allergen,
        Integer sortOrder
) {
}
