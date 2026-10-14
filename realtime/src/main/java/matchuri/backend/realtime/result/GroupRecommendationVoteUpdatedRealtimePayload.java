package matchuri.backend.realtime.result;

public record GroupRecommendationVoteUpdatedRealtimePayload(
        Long sessionId,
        RealtimeVoteProgressPayload voteProgress
) {
}
