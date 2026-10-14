package matchuri.backend.recommendation.repository;

import java.util.List;
import matchuri.backend.recommendation.entity.PersonalRecommendation;
import org.springframework.data.domain.Pageable;

public interface PersonalRecommendationRepositoryCustom {

    List<PersonalRecommendation> findRecentSelectedByMemberId(Long memberId, Pageable pageable);
}
