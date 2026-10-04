package matchuri.backend.catalog.api.query;

import java.util.List;
import matchuri.backend.catalog.entity.MenuAttributeCategory;

public interface CatalogMenuAttributeQuery {
    List<MenuAttributeCategory> findDisplayCategoriesByMenuIds(List<Long> menuIds);
}
