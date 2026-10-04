package matchuri.backend.catalog.api.query;

import java.util.List;
import matchuri.backend.catalog.result.RecommendationMenuResult;

public interface CatalogRecommendationMenuQueryService {

    List<RecommendationMenuResult> findActiveMenus();
}
