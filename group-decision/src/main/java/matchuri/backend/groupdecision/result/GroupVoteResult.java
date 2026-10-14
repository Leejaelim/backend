package matchuri.backend.groupdecision.result;

import java.time.LocalDateTime;

public record GroupVoteResult(
        Long voteId,
        Long candidateId,
        LocalDateTime votedAt
) {
}
