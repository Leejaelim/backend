package matchuri.backend.groupdecision.result;

import matchuri.backend.groupdecision.entity.GroupMemberStatus;

public record JoinGroupResult(
        Long groupId,
        GroupMemberStatus memberStatus
) {
}
