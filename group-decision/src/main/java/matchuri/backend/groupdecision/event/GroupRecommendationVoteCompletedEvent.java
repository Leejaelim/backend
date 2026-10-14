package matchuri.backend.groupdecision.event;

import matchuri.backend.groupdecision.result.GroupVoteProgressResult;

public record GroupRecommendationVoteCompletedEvent(
        Long groupId,
        Long sessionId,
        Long ownerMemberId,
        GroupVoteProgressResult voteProgress
) {
}
