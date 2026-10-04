package matchuri.backend.catalog.repository;

import java.util.List;
import matchuri.backend.catalog.entity.Ingredient;
import org.jspecify.annotations.Nullable;

public interface IngredientRepositoryCustom {

    List<Ingredient> searchActiveRestrictionIngredients(@Nullable String query, @Nullable Boolean allergen);
}
