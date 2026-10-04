package matchuri.backend.catalog.query;

public record GetRestrictionIngredientsQuery(
        String query,
        Boolean allergen
) {
}
