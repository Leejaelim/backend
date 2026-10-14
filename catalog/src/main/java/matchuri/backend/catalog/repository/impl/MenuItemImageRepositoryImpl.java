package matchuri.backend.catalog.repository.impl;

import static matchuri.backend.media.entity.QImageAsset.imageAsset;
import static matchuri.backend.catalog.entity.QMenuItemImage.menuItemImage;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import matchuri.backend.catalog.repository.MenuItemImageRepositoryCustom;
import matchuri.backend.catalog.repository.MenuThumbnailQueryRow;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MenuItemImageRepositoryImpl implements MenuItemImageRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<MenuThumbnailQueryRow> findThumbnailRowsByMenuIds(Collection<Long> menuIds) {
        if (menuIds.isEmpty()) {
            return List.of();
        }

        return jpaQueryFactory
                .select(Projections.constructor(
                        MenuThumbnailQueryRow.class,
                        menuItemImage.menu.id,
                        imageAsset.objectKey
                ))
                .from(menuItemImage)
                .join(menuItemImage.imageAsset, imageAsset)
                .where(menuItemImage.menu.id.in(menuIds))
                .fetch();
    }
}
