package matchuri.backend.groupdecision.repository;

public record GroupRecommendationVoteQueryRow(
        Long memberId,
        Long candidateId
) {
}
