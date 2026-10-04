package matchuri.backend.catalog.api.query;

import java.util.List;
import java.util.Map;
import matchuri.backend.catalog.result.MenuAttributeCategoryResult;

public interface CatalogMenuAttributeQueryService {
    Map<Long, List<MenuAttributeCategoryResult>> findDisplayCategoriesByMenuIds(List<Long> menuIds);
}
