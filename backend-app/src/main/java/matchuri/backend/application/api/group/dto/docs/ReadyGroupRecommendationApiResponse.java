package matchuri.backend.application.api.group.dto.docs;

import matchuri.backend.application.api.group.dto.response.ReadyGroupRecommendationResponse;
import matchuri.backend.shared.api.ErrorResponse;

public record ReadyGroupRecommendationApiResponse(
        boolean success,
        ReadyGroupRecommendationResponse data,
        ErrorResponse error
) {
}
