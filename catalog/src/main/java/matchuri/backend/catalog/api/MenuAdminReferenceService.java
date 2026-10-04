package matchuri.backend.catalog.api;

import java.util.List;
import matchuri.backend.catalog.command.CreateAdminAttributeCategoryCommand;
import matchuri.backend.catalog.command.CreateAdminIngredientCommand;
import matchuri.backend.catalog.command.CreateAdminMenuItemCommand;
import matchuri.backend.catalog.command.UpdateAdminAttributeCategoryCommand;
import matchuri.backend.catalog.command.UpdateAdminIngredientCommand;
import matchuri.backend.catalog.command.UpdateAdminMenuItemCommand;
import matchuri.backend.catalog.command.UpdateAdminMenuItemReferencesCommand;
import matchuri.backend.catalog.result.AdminAttributeCategoryResult;
import matchuri.backend.catalog.result.AdminIngredientResult;
import matchuri.backend.catalog.result.AdminMenuItemDetailResult;
import matchuri.backend.catalog.result.AdminMenuItemResult;

public interface MenuAdminReferenceService {

    List<AdminAttributeCategoryResult> getAttributeCategories();

    List<AdminIngredientResult> getIngredients();

    List<AdminMenuItemResult> getMenuItems();

    AdminMenuItemDetailResult getMenuItemDetail(Long menuItemId);

    AdminAttributeCategoryResult createAttributeCategory(CreateAdminAttributeCategoryCommand command);

    AdminIngredientResult createIngredient(CreateAdminIngredientCommand command);

    AdminMenuItemDetailResult createMenuItem(CreateAdminMenuItemCommand command);

    AdminAttributeCategoryResult updateAttributeCategory(UpdateAdminAttributeCategoryCommand command);

    AdminIngredientResult updateIngredient(UpdateAdminIngredientCommand command);

    AdminMenuItemResult updateMenuItem(UpdateAdminMenuItemCommand command);

    AdminMenuItemDetailResult updateMenuItemReferences(UpdateAdminMenuItemReferencesCommand command);

    AdminMenuItemResult deactivateMenuItem(Long menuItemId);

    AdminAttributeCategoryResult deactivateAttributeCategory(Long attributeCategoryId);

    AdminIngredientResult deactivateIngredient(Long ingredientId);
}
