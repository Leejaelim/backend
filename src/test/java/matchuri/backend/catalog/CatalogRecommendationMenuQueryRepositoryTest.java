package matchuri.backend.catalog;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import matchuri.backend.application.config.JpaConfig;
import matchuri.backend.catalog.api.query.CatalogRecommendationMenuQueryService;
import matchuri.backend.catalog.entity.AttributeCategory;
import matchuri.backend.catalog.entity.CategoryType;
import matchuri.backend.catalog.entity.Ingredient;
import matchuri.backend.catalog.entity.MenuAttributeCategory;
import matchuri.backend.catalog.entity.MenuIngredient;
import matchuri.backend.catalog.entity.MenuItem;
import matchuri.backend.catalog.repository.AttributeCategoryRepository;
import matchuri.backend.catalog.repository.IngredientRepository;
import matchuri.backend.catalog.repository.MenuAttributeCategoryRepository;
import matchuri.backend.catalog.repository.MenuIngredientRepository;
import matchuri.backend.catalog.repository.MenuItemRepository;
import matchuri.backend.catalog.result.RecommendationMenuResult;
import matchuri.backend.catalog.service.CatalogRecommendationMenuQueryServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest(showSql = false)
@Import({JpaConfig.class, CatalogRecommendationMenuQueryServiceImpl.class})
@ActiveProfiles("test")
class CatalogRecommendationMenuQueryRepositoryTest {

    @Autowired private CatalogRecommendationMenuQueryService queryService;
    @Autowired private MenuItemRepository menus;
    @Autowired private AttributeCategoryRepository categories;
    @Autowired private IngredientRepository ingredients;
    @Autowired private MenuAttributeCategoryRepository menuCategories;
    @Autowired private MenuIngredientRepository menuIngredients;

    @Test
    @DisplayName("추천 메뉴 조회는 활성 메뉴만 반환하고 연결된 속성·재료의 활성 여부와 무관하게 ID를 보존한다")
    void returnsActiveMenusWithAllLinkedIdsAndEmptyMissingAssociations() {
        MenuItem inactive = new MenuItem("INACTIVE", "중단 메뉴", "");
        inactive.deactivate();
        menus.save(inactive);
        MenuItem linked = menus.save(new MenuItem("LINKED", "속성·재료 있는 메뉴", ""));
        MenuItem plain = menus.save(new MenuItem("PLAIN", "속성·재료 없는 메뉴", ""));

        AttributeCategory activeCategory = categories.save(
                new AttributeCategory(CategoryType.FLAVOR, "SPICY", "매운맛", 10));
        AttributeCategory inactiveCategory = new AttributeCategory(CategoryType.FLAVOR, "OLD", "기존 속성", 20);
        inactiveCategory.deactivate();
        categories.save(inactiveCategory);
        Ingredient activeIngredient = ingredients.save(new Ingredient("RICE", "쌀", false, 10));
        Ingredient inactiveIngredient = new Ingredient("OLD_INGREDIENT", "기존 재료", true, 20);
        inactiveIngredient.deactivate();
        ingredients.save(inactiveIngredient);

        menuCategories.saveAll(List.of(
                new MenuAttributeCategory(linked, activeCategory),
                new MenuAttributeCategory(linked, inactiveCategory),
                new MenuAttributeCategory(inactive, activeCategory)
        ));
        menuIngredients.saveAll(List.of(
                new MenuIngredient(linked, activeIngredient),
                new MenuIngredient(linked, inactiveIngredient),
                new MenuIngredient(inactive, activeIngredient)
        ));

        assertThat(queryService.findActiveMenus()).containsExactly(
                new RecommendationMenuResult(linked.getId(), "LINKED", "속성·재료 있는 메뉴",
                        List.of(activeCategory.getId(), inactiveCategory.getId()),
                        List.of(activeIngredient.getId(), inactiveIngredient.getId())),
                new RecommendationMenuResult(plain.getId(), "PLAIN", "속성·재료 없는 메뉴", List.of(), List.of())
        );
    }

    @Test
    @DisplayName("추천 가능한 활성 메뉴가 없으면 빈 결과를 반환한다")
    void returnsEmptyWhenAllMenusAreInactive() {
        MenuItem inactive = new MenuItem("INACTIVE_ONLY", "중단 메뉴", "");
        inactive.deactivate();
        menus.save(inactive);

        assertThat(queryService.findActiveMenus()).isEmpty();
    }
}
