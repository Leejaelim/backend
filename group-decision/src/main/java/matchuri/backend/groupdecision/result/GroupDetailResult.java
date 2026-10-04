package matchuri.backend.groupdecision.result;

import java.math.BigDecimal;
import java.util.List;
import matchuri.backend.groupdecision.entity.GroupRoomStatus;

public record GroupDetailResult(
        Long id,
        String name,
        String inviteCode,
        BigDecimal latitude,
        BigDecimal longitude,
        Integer radiusMeters,
        String address,
        GroupRoomStatus status,
        List<GroupMemberSummaryResult> members,
        GroupRecommendationResult recentlyRecommendation
) {
}
