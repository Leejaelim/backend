package matchuri.backend.application.api.group.dto.docs;

import matchuri.backend.application.api.group.dto.response.GroupRecommendationSummaryResponse;
import matchuri.backend.shared.api.ErrorResponse;
import matchuri.backend.shared.api.PageResponse;

public record GroupRecommendationSummaryPageApiResponse(
        boolean success,
        PageResponse<GroupRecommendationSummaryResponse> data,
        ErrorResponse error
) {
}
