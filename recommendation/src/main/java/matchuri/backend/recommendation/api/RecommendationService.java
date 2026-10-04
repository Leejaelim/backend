package matchuri.backend.recommendation.api;

import java.util.List;
import matchuri.backend.recommendation.command.GuestPersonalRecommendationCommand;
import matchuri.backend.recommendation.command.SelectPersonalRecommendationCommand;
import matchuri.backend.recommendation.entity.PersonalRecommendationRerollType;
import matchuri.backend.recommendation.result.GuestPersonalRecommendationResult;
import matchuri.backend.recommendation.result.PersonalRecommendationCandidateResult;
import matchuri.backend.recommendation.result.PersonalRecommendationHistoryResult;
import matchuri.backend.recommendation.result.PersonalRecommendationResult;
import matchuri.backend.recommendation.result.PersonalRecommendationHomeResult;
import matchuri.backend.recommendation.result.PersonalRecommendationSummaryResult;
import matchuri.backend.recommendation.result.SelectPersonalRecommendationResult;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;

public interface RecommendationService {
    PersonalRecommendationHomeResult getHomeRecommendations(Long memberId);

    PersonalRecommendationResult createPersonalRecommendation(Long memberId, String contextJson);

    PersonalRecommendationResult rerollPersonalRecommendation(Long memberId, Long sourcePersonalRecommendationId, PersonalRecommendationRerollType rerollType, String contextJson);

    GuestPersonalRecommendationResult createGuestPersonalRecommendation(GuestPersonalRecommendationCommand command);

    PersonalRecommendationResult getPersonalRecommendation(Long memberId, Long personalRecommendationId);

    List<PersonalRecommendationCandidateResult> getPersonalRecommendationCandidates(Long memberId, Long personalRecommendationId);

    Page<@NonNull PersonalRecommendationSummaryResult> getMyPersonalRecommendations(Long memberId, int page, int size);

    Page<@NonNull PersonalRecommendationHistoryResult> getMyPersonalRecommendationHistories(
            Long memberId,
            int page,
            int size
    );

    SelectPersonalRecommendationResult selectPersonalRecommendationCandidate(Long memberId, SelectPersonalRecommendationCommand command);
}
