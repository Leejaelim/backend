package matchuri.backend.catalog;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import matchuri.backend.application.config.JpaConfig;
import matchuri.backend.catalog.api.query.CatalogMenuAttributeQueryService;
import matchuri.backend.catalog.entity.AttributeCategory;
import matchuri.backend.catalog.entity.CategoryType;
import matchuri.backend.catalog.entity.MenuAttributeCategory;
import matchuri.backend.catalog.entity.MenuItem;
import matchuri.backend.catalog.repository.AttributeCategoryRepository;
import matchuri.backend.catalog.repository.MenuAttributeCategoryRepository;
import matchuri.backend.catalog.repository.MenuItemRepository;
import matchuri.backend.catalog.result.MenuAttributeCategoryResult;
import matchuri.backend.catalog.service.CatalogMenuAttributeQueryServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest(showSql = false)
@Import({JpaConfig.class, CatalogMenuAttributeQueryServiceImpl.class})
@ActiveProfiles("test")
class CatalogMenuAttributeQueryRepositoryTest {

    @Autowired private CatalogMenuAttributeQueryService queryService;
    @Autowired private MenuItemRepository menus;
    @Autowired private AttributeCategoryRepository categories;
    @Autowired private MenuAttributeCategoryRepository menuCategories;

    @Test
    @DisplayName("표시용 속성은 요청한 메뉴별로 활성 속성만 유형·표시순서·ID 순으로 반환한다")
    void returnsActiveCategoriesByRequestedMenuInDisplayOrder() {
        MenuItem linked = menus.save(new MenuItem("LINKED", "연결 메뉴", ""));
        MenuItem inactiveMenu = new MenuItem("INACTIVE", "중단 메뉴", "");
        inactiveMenu.deactivate();
        menus.save(inactiveMenu);
        MenuItem plain = menus.save(new MenuItem("PLAIN", "활성 속성 없는 메뉴", ""));
        MenuItem excluded = menus.save(new MenuItem("EXCLUDED", "요청하지 않은 메뉴", ""));

        AttributeCategory later = categories.save(
                new AttributeCategory(CategoryType.FLAVOR, "LATER", "나중 속성", 20));
        AttributeCategory earlier = categories.save(
                new AttributeCategory(CategoryType.FLAVOR, "EARLIER", "먼저 속성", 10));
        AttributeCategory tied = categories.save(
                new AttributeCategory(CategoryType.FLAVOR, "TIED", "같은 순서 속성", 20));
        AttributeCategory cooking = categories.save(
                new AttributeCategory(CategoryType.COOKING_METHOD, "COOKING", "조리법", 100));
        AttributeCategory inactive = new AttributeCategory(CategoryType.FLAVOR, "OLD", "중단 속성", 0);
        inactive.deactivate();
        categories.save(inactive);

        menuCategories.saveAll(List.of(
                new MenuAttributeCategory(linked, tied),
                new MenuAttributeCategory(linked, inactive),
                new MenuAttributeCategory(linked, later),
                new MenuAttributeCategory(linked, earlier),
                new MenuAttributeCategory(linked, cooking),
                new MenuAttributeCategory(inactiveMenu, later),
                new MenuAttributeCategory(plain, inactive),
                new MenuAttributeCategory(excluded, cooking)
        ));

        Map<Long, List<MenuAttributeCategoryResult>> results = queryService.findDisplayCategoriesByMenuIds(
                List.of(linked.getId(), inactiveMenu.getId(), plain.getId(), -1L));

        assertThat(results).containsOnlyKeys(linked.getId(), inactiveMenu.getId());
        assertThat(results.get(linked.getId())).containsExactly(
                new MenuAttributeCategoryResult(cooking.getId(), CategoryType.COOKING_METHOD, "COOKING", "조리법", 100),
                new MenuAttributeCategoryResult(earlier.getId(), CategoryType.FLAVOR, "EARLIER", "먼저 속성", 10),
                new MenuAttributeCategoryResult(later.getId(), CategoryType.FLAVOR, "LATER", "나중 속성", 20),
                new MenuAttributeCategoryResult(tied.getId(), CategoryType.FLAVOR, "TIED", "같은 순서 속성", 20)
        );
        assertThat(results.get(inactiveMenu.getId())).containsExactly(
                new MenuAttributeCategoryResult(later.getId(), CategoryType.FLAVOR, "LATER", "나중 속성", 20)
        );
    }

    @Test
    @DisplayName("요청한 메뉴가 없거나 표시할 속성이 없으면 빈 결과를 반환한다")
    void returnsEmptyWhenNoMenusOrCategoriesMatch() {
        assertThat(queryService.findDisplayCategoriesByMenuIds(List.of())).isEmpty();
        assertThat(queryService.findDisplayCategoriesByMenuIds(List.of(-1L))).isEmpty();
    }
}
