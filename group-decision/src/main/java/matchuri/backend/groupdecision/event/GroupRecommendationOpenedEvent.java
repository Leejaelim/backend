package matchuri.backend.groupdecision.event;

import java.util.List;
import matchuri.backend.groupdecision.entity.GroupRecommendationStatus;
import matchuri.backend.groupdecision.result.GroupRecommendationCandidateResult;
import matchuri.backend.groupdecision.result.GroupVoteProgressResult;

public record GroupRecommendationOpenedEvent(
        Long groupId,
        Long sessionId,
        GroupRecommendationStatus status,
        List<GroupRecommendationCandidateResult> candidates,
        GroupVoteProgressResult voteProgress
) {
}
