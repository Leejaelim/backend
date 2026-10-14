package matchuri.backend.shared.api;

public record ValidationErrorDetail(
        String source,
        String field,
        String reason
) {
}
