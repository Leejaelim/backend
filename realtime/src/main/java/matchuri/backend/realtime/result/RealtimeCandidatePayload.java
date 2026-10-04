package matchuri.backend.realtime.result;

import matchuri.backend.groupdecision.result.GroupRecommendationCandidateResult;

public record RealtimeCandidatePayload(
        Long candidateId,
        Long menuId,
        String menuName,
        Integer rankNo
) {
    public static RealtimeCandidatePayload from(GroupRecommendationCandidateResult candidate) {
        return new RealtimeCandidatePayload(
                candidate.candidateId(),
                candidate.menuId(),
                candidate.menuName(),
                candidate.rankNo()
        );
    }
}
