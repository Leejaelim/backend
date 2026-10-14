package matchuri.backend.recommendation.query;

import lombok.RequiredArgsConstructor;
import matchuri.backend.identity.member.spi.OpenPersonalRecommendationQuery;
import matchuri.backend.recommendation.entity.PersonalRecommendation;
import matchuri.backend.recommendation.entity.PersonalRecommendationStatus;
import matchuri.backend.recommendation.repository.PersonalRecommendationRepository;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class OpenPersonalRecommendationQueryAdapter implements OpenPersonalRecommendationQuery {
    private final PersonalRecommendationRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Optional<Long> findOpenPersonalRecommendationId(Long memberId) {
        return repository.findFirstByMemberIdAndStatusAndSelectedCandidateIsNullAndClosedAtIsNullOrderByRequestedAtDescIdDesc(
                        memberId, PersonalRecommendationStatus.OPEN)
                .map(PersonalRecommendation::getId);
    }
}
