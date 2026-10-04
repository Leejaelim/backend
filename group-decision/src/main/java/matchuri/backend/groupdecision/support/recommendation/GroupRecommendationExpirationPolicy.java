package matchuri.backend.groupdecision.support.recommendation;

import java.time.LocalDateTime;
import java.util.List;
import matchuri.backend.groupdecision.entity.GroupRecommendation;
import matchuri.backend.groupdecision.entity.GroupRecommendationStatus;
import org.springframework.stereotype.Component;

@Component
public class GroupRecommendationExpirationPolicy {

    static final long EXPIRATION_HOURS = 24;
    private static final List<GroupRecommendationStatus> EXPIRABLE_STATUSES = List.of(
            GroupRecommendationStatus.PREPARING,
            GroupRecommendationStatus.OPEN
    );

    public boolean isExpired(GroupRecommendation recommendation, LocalDateTime now) {
        return EXPIRABLE_STATUSES.contains(recommendation.getStatus())
                && recommendation.getEndedAt() == null
                && !recommendation.getCreatedAt().plusHours(EXPIRATION_HOURS).isAfter(now);
    }

    public LocalDateTime activeThreshold(LocalDateTime now) {
        return now.minusHours(EXPIRATION_HOURS);
    }
}
