package matchuri.backend.application.api.group.dto.docs;

import matchuri.backend.application.api.group.dto.response.GroupRecommendationReadinessResponse;
import matchuri.backend.shared.api.ErrorResponse;

public record GroupRecommendationReadinessApiResponse(
        boolean success,
        GroupRecommendationReadinessResponse data,
        ErrorResponse error
) {
}
