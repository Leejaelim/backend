package matchuri.backend.catalog.api.query;

import java.util.Collection;
import java.util.List;
import matchuri.backend.catalog.entity.Ingredient;

public interface CatalogIngredientQuery {
    List<Ingredient> findAllByIdInAndActiveTrue(Collection<Long> ids);
}
