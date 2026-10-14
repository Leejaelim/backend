package matchuri.backend.groupdecision.repository;

public record GroupCandidateVoteCountRow(
        Long candidateId,
        Long voteCount
) {
}
