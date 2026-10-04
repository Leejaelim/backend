package matchuri.backend.catalog.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import matchuri.backend.catalog.entity.MenuItem;
import org.jspecify.annotations.NullMarked;
import matchuri.backend.catalog.api.query.CatalogMenuQuery;
import org.springframework.data.jpa.repository.JpaRepository;

@NullMarked
public interface MenuItemRepository extends JpaRepository<MenuItem, Long>, CatalogMenuQuery, MenuItemRepositoryCustom {

    boolean existsByCode(String code);

    Optional<MenuItem> findByCode(String code);

    List<MenuItem> findAllByOrderByIdAsc();

    List<MenuItem> findAllByIdInAndActiveTrue(Collection<Long> ids);

}
