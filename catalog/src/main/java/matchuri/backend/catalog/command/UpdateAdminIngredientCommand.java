package matchuri.backend.catalog.command;

public record UpdateAdminIngredientCommand(
        Long ingredientId,
        String name,
        Boolean allergen,
        Integer sortOrder,
        Boolean isActive
) {
}
