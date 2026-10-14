package matchuri.backend.catalog.command;

public record UpdateAdminMenuItemCommand(
        Long menuItemId,
        String name,
        String description,
        Boolean isActive
) {
}
