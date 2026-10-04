package matchuri.backend.realtime.result;

public record GroupRecommendationVoteCompletedRealtimePayload(
        Long sessionId,
        RealtimeVoteProgressPayload voteProgress,
        boolean finalizeRequired
) {
}
