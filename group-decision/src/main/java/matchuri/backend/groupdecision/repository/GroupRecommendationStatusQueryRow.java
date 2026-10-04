package matchuri.backend.groupdecision.repository;

import matchuri.backend.groupdecision.entity.GroupRecommendationStatus;

public record GroupRecommendationStatusQueryRow(
        Long roomId,
        GroupRecommendationStatus status
) {
}
