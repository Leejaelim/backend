package matchuri.backend.catalog.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import matchuri.backend.catalog.api.query.CatalogMenuAttributeQueryService;
import matchuri.backend.catalog.entity.AttributeCategory;
import matchuri.backend.catalog.repository.MenuAttributeCategoryRepository;
import matchuri.backend.catalog.result.MenuAttributeCategoryResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CatalogMenuAttributeQueryServiceImpl implements CatalogMenuAttributeQueryService {

    private final MenuAttributeCategoryRepository menuAttributeCategoryRepository;

    @Override
    public Map<Long, List<MenuAttributeCategoryResult>> findDisplayCategoriesByMenuIds(List<Long> menuIds) {
        if (menuIds.isEmpty()) {
            return Map.of();
        }

        Map<Long, List<MenuAttributeCategoryResult>> categoriesByMenuId = menuAttributeCategoryRepository
                .findDisplayCategoriesByMenuIds(menuIds).stream()
                .collect(Collectors.groupingBy(
                        mapping -> mapping.getMenu().getId(),
                        Collectors.mapping(
                                mapping -> toResult(mapping.getAttributeCategory()),
                                Collectors.toUnmodifiableList()
                        )
                ));
        return Map.copyOf(categoriesByMenuId);
    }

    private MenuAttributeCategoryResult toResult(AttributeCategory category) {
        return new MenuAttributeCategoryResult(
                category.getId(), category.getCategoryType(),
                category.getCode(), category.getName(), category.getSortOrder()
        );
    }
}
