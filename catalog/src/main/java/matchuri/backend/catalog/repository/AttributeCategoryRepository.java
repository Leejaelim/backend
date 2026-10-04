package matchuri.backend.catalog.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import matchuri.backend.catalog.entity.AttributeCategory;
import matchuri.backend.catalog.entity.CategoryType;
import org.jspecify.annotations.NullMarked;
import matchuri.backend.catalog.api.query.CatalogAttributeQuery;
import org.springframework.data.jpa.repository.JpaRepository;

@NullMarked
public interface AttributeCategoryRepository extends JpaRepository<AttributeCategory, Long>, CatalogAttributeQuery {

    boolean existsByCategoryTypeAndCode(CategoryType categoryType, String code);

    Optional<AttributeCategory> findByCategoryTypeAndCode(CategoryType categoryType, String code);

    List<AttributeCategory> findAllByOrderByCategoryTypeAscSortOrderAscIdAsc();

    List<AttributeCategory> findAllByActiveTrueOrderByCategoryTypeAscSortOrderAscIdAsc();

    List<AttributeCategory> findAllByActiveTrueAndCategoryTypeInOrderByCategoryTypeAscSortOrderAscIdAsc(
            Collection<CategoryType> categoryTypes
    );

    List<AttributeCategory> findAllByIdInAndActiveTrue(Collection<Long> ids);
}
