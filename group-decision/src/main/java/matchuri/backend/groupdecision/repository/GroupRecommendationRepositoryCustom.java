package matchuri.backend.groupdecision.repository;

import java.util.Collection;
import java.util.List;
import matchuri.backend.groupdecision.entity.GroupRecommendation;

public interface GroupRecommendationRepositoryCustom {

    List<GroupRecommendationStatusQueryRow> findLatestStatusesByRoomIds(Collection<Long> roomIds);

    List<GroupRecommendation> findHistoryForActiveMember(Long memberId);
}
