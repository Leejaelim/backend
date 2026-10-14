package matchuri.backend.groupdecision.repository;

import java.util.List;

public interface GroupRecommendationCandidateRepositoryCustom {

    List<GroupRecommendationCandidateQueryRow> findCandidateRowsWithVoteCounts(Long recommendationId);
}
