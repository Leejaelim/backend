package matchuri.backend.groupdecision.result;

import java.time.LocalDateTime;
import java.util.List;
import matchuri.backend.groupdecision.entity.GroupRecommendationStatus;

public record GroupRecommendationResult(
        Long sessionId,
        GroupRecommendationStatus status,
        String contextJson,
        GroupRecommendationReadinessProgressResult readiness,
        List<GroupRecommendationCandidateResult> candidates,
        GroupVoteProgressResult voteProgress,
        List<GroupMemberVoteResult> memberVotes,
        GroupRecommendationCandidateResult finalCandidate,
        LocalDateTime createdAt
) {
}
