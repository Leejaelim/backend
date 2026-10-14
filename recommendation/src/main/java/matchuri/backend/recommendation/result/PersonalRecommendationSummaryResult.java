package matchuri.backend.recommendation.result;

import java.time.LocalDateTime;
import matchuri.backend.recommendation.entity.PersonalRecommendation;
import matchuri.backend.recommendation.entity.PersonalRecommendationStatus;

public record PersonalRecommendationSummaryResult(
        Long id,
        PersonalRecommendationStatus status,
        LocalDateTime requestedAt,
        LocalDateTime closedAt
) {
    public static PersonalRecommendationSummaryResult from(PersonalRecommendation personalRecommendation) {
        return new PersonalRecommendationSummaryResult(
                personalRecommendation.getId(),
                personalRecommendation.getStatus(),
                personalRecommendation.getRequestedAt(),
                personalRecommendation.getClosedAt()
        );
    }
}
