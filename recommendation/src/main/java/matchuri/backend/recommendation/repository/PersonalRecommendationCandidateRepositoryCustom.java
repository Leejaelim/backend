package matchuri.backend.recommendation.repository;

import java.util.List;
import matchuri.backend.recommendation.entity.PersonalRecommendationCandidate;

public interface PersonalRecommendationCandidateRepositoryCustom {

    List<PersonalRecommendationCandidateQueryRow> findCandidateRowsByPersonalRecommendationId(Long personalRecommendationId);

    List<PersonalRecommendationCandidate> findRepresentativeCandidates(List<Long> personalRecommendationIds);
}
