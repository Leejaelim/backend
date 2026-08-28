package matchuri.backend.domain.menu.repository;

import java.util.List;
import matchuri.backend.domain.menu.entity.AttributeCategory;
import matchuri.backend.domain.menu.entity.MenuAttributeCategory;
import matchuri.backend.domain.menu.entity.MenuItem;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

// TODO: 의도가 명확한 메서드명으로 변경하기
@NullMarked
public interface MenuAttributeCategoryRepository extends JpaRepository<MenuAttributeCategory, Long> {

    @Query("""
            select mapping from MenuAttributeCategory mapping
            join fetch mapping.attributeCategory category
            where mapping.menu.id in :menuIds
              and category.active = true
            order by category.categoryType asc, category.sortOrder asc, category.id asc
            """)
    List<MenuAttributeCategory> findDisplayCategoriesByMenuIds(@Param("menuIds") List<Long> menuIds);

    boolean existsByMenuAndAttributeCategory(MenuItem menu, AttributeCategory attributeCategory);

    List<MenuAttributeCategory> findAllByMenuIdOrderByAttributeCategoryCategoryTypeAscAttributeCategorySortOrderAscAttributeCategoryIdAsc(
            Long menuId
    );

    List<MenuAttributeCategory> findAllByMenuIdAndAttributeCategoryActiveTrueOrderByAttributeCategoryCategoryTypeAscAttributeCategorySortOrderAscAttributeCategoryIdAsc(
            Long menuId
    );
}
