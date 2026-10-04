package matchuri.backend.catalog.repository;

import java.util.Collection;
import java.util.List;
import matchuri.backend.catalog.entity.Ingredient;
import matchuri.backend.catalog.entity.MenuIngredient;
import matchuri.backend.catalog.entity.MenuItem;
import org.jspecify.annotations.NullMarked;
import matchuri.backend.catalog.api.query.CatalogMenuIngredientQuery;
import org.springframework.data.jpa.repository.JpaRepository;

@NullMarked
public interface MenuIngredientRepository extends JpaRepository<MenuIngredient, Long>, CatalogMenuIngredientQuery, MenuIngredientRepositoryCustom {

    boolean existsByMenuAndIngredient(MenuItem menu, Ingredient ingredient);

    List<MenuIngredient> findAllByMenuIdOrderByIngredientSortOrderAscIngredientIdAsc(Long menuId);

    List<MenuIngredient> findAllByMenuIdAndIngredientActiveTrueOrderByIngredientSortOrderAscIngredientIdAsc(Long menuId);

    List<MenuIngredient> findAllByIngredientIdNotIn(Collection<Long> ingredientIds);
}
