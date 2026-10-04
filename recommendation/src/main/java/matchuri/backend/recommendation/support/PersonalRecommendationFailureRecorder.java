package matchuri.backend.recommendation.support;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import matchuri.backend.recommendation.repository.PersonalRecommendationRepository;
import matchuri.backend.shared.transaction.RollbackRecordExecutor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PersonalRecommendationFailureRecorder {

    private final PersonalRecommendationRepository repository;
    private final RollbackRecordExecutor rollbackRecordExecutor;

    public void expireAfterRollback(Long id, LocalDateTime expiredAt) {
        rollbackRecordExecutor.afterRollback("personal-recommendation-expiration", () -> repository.findById(id)
                .filter(recommendation -> recommendation.isOpen()
                        && recommendation.getClosedAt() == null
                        && recommendation.getSelectedCandidate() == null)
                .ifPresent(recommendation -> recommendation.expire(expiredAt)));
    }
}
