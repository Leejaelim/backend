package matchuri.backend.catalog.repository;

import java.util.Collection;
import java.util.List;

public interface MenuItemImageRepositoryCustom {

    List<MenuThumbnailQueryRow> findThumbnailRowsByMenuIds(Collection<Long> menuIds);
}
