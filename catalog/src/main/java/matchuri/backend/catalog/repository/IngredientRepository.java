package matchuri.backend.catalog.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import matchuri.backend.catalog.entity.Ingredient;
import org.jspecify.annotations.NullMarked;
import matchuri.backend.catalog.api.query.CatalogIngredientQuery;
import org.springframework.data.jpa.repository.JpaRepository;

@NullMarked
public interface IngredientRepository extends JpaRepository<Ingredient, Long>, CatalogIngredientQuery, IngredientRepositoryCustom {

    boolean existsByCode(String code);

    Optional<Ingredient> findByCode(String code);

    List<Ingredient> findAllByOrderBySortOrderAscIdAsc();

    List<Ingredient> findAllByIdInAndActiveTrue(Collection<Long> ids);
}
