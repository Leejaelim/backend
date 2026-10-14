package matchuri.backend.catalog.result;

import matchuri.backend.catalog.entity.MenuItem;

public record AdminMenuItemResult(
        Long id,
        String code,
        String name,
        String description,
        boolean isActive
) {

    public static AdminMenuItemResult from(MenuItem menuItem) {
        return new AdminMenuItemResult(
                menuItem.getId(),
                menuItem.getCode(),
                menuItem.getName(),
                menuItem.getDescription(),
                menuItem.isActive()
        );
    }
}
