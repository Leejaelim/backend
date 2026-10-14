package matchuri.backend.groupdecision.repository;

import java.util.List;
import java.util.Optional;
import matchuri.backend.groupdecision.entity.GroupRecommendationCandidate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRecommendationCandidateRepository extends JpaRepository<GroupRecommendationCandidate, Long>,
        GroupRecommendationCandidateRepositoryCustom {

    List<GroupRecommendationCandidate> findAllByGroupRecommendationIdOrderByRankNoAsc(Long recommendationId);

    Optional<GroupRecommendationCandidate> findByIdAndGroupRecommendationId(Long id, Long recommendationId);
}
