package matchuri.backend.catalog.repository;

import java.util.Collection;
import java.util.List;

public interface MenuIngredientRepositoryCustom {

    List<MenuRestrictionIngredientRow> findActiveRowsByMenuId(Long menuId);

    List<MenuIngredientIdRow> findIdRowsByMenuIds(Collection<Long> menuIds);
}
