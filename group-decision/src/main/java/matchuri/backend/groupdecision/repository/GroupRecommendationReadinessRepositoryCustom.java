package matchuri.backend.groupdecision.repository;

import matchuri.backend.groupdecision.entity.GroupRecommendationReadinessStatus;

public interface GroupRecommendationReadinessRepositoryCustom {

    long countActiveMemberReadinessByRecommendationIdAndStatus(Long groupRecommendationId, Long roomId, GroupRecommendationReadinessStatus status);
}
