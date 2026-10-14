package matchuri.backend.groupdecision.service;

import java.util.List;
import lombok.NonNull;
import matchuri.backend.groupdecision.command.CreateGroupRecommendationCommand;
import matchuri.backend.groupdecision.command.FinalizeGroupRecommendationCommand;
import matchuri.backend.groupdecision.entity.GroupRecommendationRerollType;
import matchuri.backend.groupdecision.result.CreateGroupRecommendationResult;
import matchuri.backend.groupdecision.result.FinalizeGroupRecommendationResult;
import matchuri.backend.groupdecision.result.GroupHomeActivityResult;
import matchuri.backend.groupdecision.result.GroupRecommendationCandidateListResult;
import matchuri.backend.groupdecision.result.GroupRecommendationReadinessResult;
import matchuri.backend.groupdecision.result.GroupRecommendationDetailResult;
import matchuri.backend.groupdecision.result.GroupRecommendationResult;
import matchuri.backend.groupdecision.result.GroupRecommendationSummaryResult;
import matchuri.backend.groupdecision.result.GroupRecommendationV2SummaryResult;
import matchuri.backend.groupdecision.result.GroupVoteResult;
import matchuri.backend.groupdecision.result.ReadyGroupRecommendationResult;
import org.springframework.data.domain.Page;

public interface GroupRecommendationService {

    List<GroupHomeActivityResult> getHomeActivities(Long memberId);

    CreateGroupRecommendationResult createGroupRecommendation(Long memberId, CreateGroupRecommendationCommand command);

    CreateGroupRecommendationResult rerollGroupRecommendation(Long memberId, Long groupId, Long sessionId, GroupRecommendationRerollType rerollType, String contextJson);

    GroupRecommendationDetailResult getGroupRecommendation(Long memberId, Long groupId, Long sessionId);

    GroupRecommendationCandidateListResult getGroupRecommendationCandidates(Long memberId, Long groupId, Long sessionId);

    Page<@NonNull GroupRecommendationSummaryResult> getGroupRecommendations(Long memberId, Long groupId, int page, int size);

    Page<@NonNull GroupRecommendationV2SummaryResult> getGroupRecommendationsV2(
            Long memberId,
            Long groupId,
            int page,
            int size
    );

    GroupRecommendationReadinessResult getGroupRecommendationReadiness(Long memberId, Long groupId, Long sessionId);

    ReadyGroupRecommendationResult readyGroupRecommendation(Long memberId, Long groupId, Long sessionId);

    GroupVoteResult voteGroupRecommendation(Long memberId, Long groupId, Long sessionId, Long candidateId);

    FinalizeGroupRecommendationResult finalizeGroupRecommendation(Long memberId, FinalizeGroupRecommendationCommand command);
}
