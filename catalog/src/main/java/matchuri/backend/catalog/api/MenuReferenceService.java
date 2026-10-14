package matchuri.backend.catalog.api;

import java.util.List;
import matchuri.backend.catalog.query.GetAttributeCategoriesQuery;
import matchuri.backend.catalog.query.GetRestrictionIngredientsQuery;
import matchuri.backend.catalog.query.SearchMenuItemsQuery;
import matchuri.backend.catalog.result.AttributeCategoryResult;
import matchuri.backend.catalog.result.MenuItemDetailResult;
import matchuri.backend.catalog.result.MenuItemSummaryResult;
import matchuri.backend.catalog.result.RestrictionIngredientResult;

public interface MenuReferenceService {

    List<AttributeCategoryResult> getActiveAttributeCategories(GetAttributeCategoriesQuery query);

    List<RestrictionIngredientResult> getActiveRestrictionIngredients(GetRestrictionIngredientsQuery query);

    List<MenuItemSummaryResult> searchMenuItems(SearchMenuItemsQuery query);

    MenuItemDetailResult getMenuItem(Long menuItemId);
}
