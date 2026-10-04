package matchuri.backend.groupdecision.result;

import matchuri.backend.groupdecision.entity.GroupMemberRole;

public record GroupMemberVoteResult(
        Long memberId,
        String nickname,
        GroupMemberRole role,
        boolean isMe,
        boolean voted,
        Long candidateId
) {
}
