package matchuri.backend.catalog.api.query;

import java.util.Collection;
import java.util.List;
import matchuri.backend.catalog.entity.AttributeCategory;

public interface CatalogAttributeQuery {
    List<AttributeCategory> findAllByIdInAndActiveTrue(Collection<Long> ids);
}
