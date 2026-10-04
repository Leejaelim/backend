package matchuri.backend.groupdecision.result;

public record GroupVoteProgressResult(
        Integer totalMemberCount,
        Integer votedMemberCount
) {
}
