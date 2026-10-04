package matchuri.backend.groupdecision.repository;

import java.util.List;
import java.util.Optional;
import matchuri.backend.groupdecision.entity.GroupRecommendationVote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRecommendationVoteRepository extends JpaRepository<GroupRecommendationVote, Long>,
        GroupRecommendationVoteRepositoryCustom {

    long countByGroupRecommendationId(Long recommendationId);

    boolean existsByGroupRecommendationIdAndMemberId(Long recommendationId, Long memberId);

    List<GroupRecommendationVote> findAllByGroupRecommendationId(Long recommendationId);

    Optional<GroupRecommendationVote> findByGroupRecommendationIdAndMemberId(Long recommendationId, Long memberId);
}
