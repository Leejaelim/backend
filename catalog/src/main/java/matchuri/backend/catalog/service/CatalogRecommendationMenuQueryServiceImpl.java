package matchuri.backend.catalog.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import matchuri.backend.catalog.api.query.CatalogRecommendationMenuQueryService;
import matchuri.backend.catalog.repository.MenuAttributeCategoryIdRow;
import matchuri.backend.catalog.repository.MenuAttributeCategoryRepository;
import matchuri.backend.catalog.repository.MenuIngredientIdRow;
import matchuri.backend.catalog.repository.MenuIngredientRepository;
import matchuri.backend.catalog.repository.MenuItemRepository;
import matchuri.backend.catalog.repository.MenuRecommendationRow;
import matchuri.backend.catalog.result.RecommendationMenuResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CatalogRecommendationMenuQueryServiceImpl implements CatalogRecommendationMenuQueryService {

    private final MenuItemRepository menuItemRepository;
    private final MenuAttributeCategoryRepository menuAttributeCategoryRepository;
    private final MenuIngredientRepository menuIngredientRepository;

    @Override
    public List<RecommendationMenuResult> findActiveMenus() {
        List<MenuRecommendationRow> menuRows = menuItemRepository.findActiveRecommendationRows();
        if (menuRows.isEmpty()) {
            return List.of();
        }

        List<Long> menuIds = menuRows.stream().map(MenuRecommendationRow::menuId).toList();
        Map<Long, List<Long>> attributeCategoryIdsByMenuId = menuAttributeCategoryRepository
                .findIdRowsByMenuIds(menuIds).stream()
                .collect(Collectors.groupingBy(
                        MenuAttributeCategoryIdRow::menuId,
                        Collectors.mapping(MenuAttributeCategoryIdRow::attributeCategoryId, Collectors.toList())
                ));
        Map<Long, List<Long>> ingredientIdsByMenuId = menuIngredientRepository
                .findIdRowsByMenuIds(menuIds).stream()
                .collect(Collectors.groupingBy(
                        MenuIngredientIdRow::menuId,
                        Collectors.mapping(MenuIngredientIdRow::ingredientId, Collectors.toList())
                ));

        return menuRows.stream()
                .map(row -> new RecommendationMenuResult(
                        row.menuId(),
                        row.menuCode(),
                        row.menuName(),
                        attributeCategoryIdsByMenuId.getOrDefault(row.menuId(), List.of()),
                        ingredientIdsByMenuId.getOrDefault(row.menuId(), List.of())
                ))
                .toList();
    }
}
