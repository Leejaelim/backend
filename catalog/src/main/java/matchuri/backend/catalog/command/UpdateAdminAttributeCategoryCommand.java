package matchuri.backend.catalog.command;

public record UpdateAdminAttributeCategoryCommand(
        Long attributeCategoryId,
        String name,
        Integer sortOrder,
        Boolean isActive
) {
}
