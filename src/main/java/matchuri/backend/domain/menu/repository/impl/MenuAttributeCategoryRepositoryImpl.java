package matchuri.backend.domain.menu.repository.impl;

import static matchuri.backend.domain.menu.entity.QAttributeCategory.attributeCategory;
import static matchuri.backend.domain.menu.entity.QMenuAttributeCategory.menuAttributeCategory;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import matchuri.backend.domain.menu.entity.MenuAttributeCategory;
import matchuri.backend.domain.menu.repository.MenuAttributeCategoryRepositoryCustom;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MenuAttributeCategoryRepositoryImpl implements MenuAttributeCategoryRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<MenuAttributeCategory> findDisplayCategoriesByMenuIds(List<Long> menuIds) {
        if (menuIds.isEmpty()) {
            return List.of();
        }

        return jpaQueryFactory
                .selectFrom(menuAttributeCategory)
                .join(menuAttributeCategory.attributeCategory, attributeCategory).fetchJoin()
                .where(
                        menuAttributeCategory.menu.id.in(menuIds),
                        attributeCategory.active.isTrue()
                )
                .orderBy(
                        attributeCategory.categoryType.asc(),
                        attributeCategory.sortOrder.asc(),
                        attributeCategory.id.asc()
                )
                .fetch();
    }
}
