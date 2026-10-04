package matchuri.backend.groupdecision.event;

import matchuri.backend.groupdecision.result.GroupVoteProgressResult;

public record GroupRecommendationVoteUpdatedEvent(
        Long groupId,
        Long sessionId,
        GroupVoteProgressResult voteProgress
) {
}
