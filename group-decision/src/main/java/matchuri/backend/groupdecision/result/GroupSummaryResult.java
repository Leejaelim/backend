package matchuri.backend.groupdecision.result;

import java.time.LocalDateTime;
import matchuri.backend.groupdecision.entity.GroupRecommendationStatus;
import matchuri.backend.groupdecision.entity.GroupRoomStatus;

public record GroupSummaryResult(
        Long id,
        String name,
        GroupRoomStatus status,
        int memberCount,
        GroupRecommendationStatus latestRecommendationStatus,
        LocalDateTime createdAt
) {
}
