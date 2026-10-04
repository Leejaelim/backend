package matchuri.backend.catalog.result;

import matchuri.backend.catalog.entity.Ingredient;

public record AdminIngredientResult(
        Long id,
        String code,
        String name,
        boolean allergen,
        int sortOrder,
        boolean isActive
) {

    public static AdminIngredientResult from(Ingredient ingredient) {
        return new AdminIngredientResult(
                ingredient.getId(),
                ingredient.getCode(),
                ingredient.getName(),
                ingredient.isAllergen(),
                ingredient.getSortOrder(),
                ingredient.isActive()
        );
    }
}
