package matchuri.backend.application.api.group.dto.docs;

import matchuri.backend.application.api.group.dto.response.GroupRecommendationV2SummaryResponse;
import matchuri.backend.shared.api.ErrorResponse;
import matchuri.backend.shared.api.PageResponse;

public record GroupRecommendationV2SummaryPageApiResponse(
        boolean success,
        PageResponse<GroupRecommendationV2SummaryResponse> data,
        ErrorResponse error
) {
}
