package matchuri.backend.catalog.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import matchuri.backend.catalog.entity.MenuItemImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuItemImageRepository extends JpaRepository<MenuItemImage, Long>, MenuItemImageRepositoryCustom {

    Optional<MenuItemImage> findByMenuId(Long menuId);

    List<MenuItemImage> findAllByMenuIdIn(Collection<Long> menuIds);
}
