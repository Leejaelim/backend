package matchuri.backend.catalog.command;

public record CreateAdminIngredientCommand(
        String code,
        String name,
        boolean allergen,
        int sortOrder
) {
}
