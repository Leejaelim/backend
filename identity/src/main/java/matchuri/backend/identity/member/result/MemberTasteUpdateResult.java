package matchuri.backend.identity.member.result;

public record MemberTasteUpdateResult(
        MemberTasteProfileSummaryResult profile,
        Long openPersonalRecommendationId
) {
}
