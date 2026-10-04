package matchuri.backend.catalog.api.query;

import java.util.List;
import matchuri.backend.catalog.entity.MenuIngredient;

public interface CatalogMenuIngredientQuery {
    List<MenuIngredient> findAll();
}
