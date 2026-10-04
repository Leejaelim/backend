package matchuri.backend.groupdecision.result;

import java.time.LocalDateTime;
import matchuri.backend.groupdecision.entity.GroupMemberStatus;

public record LeaveGroupResult(
        Long groupId,
        GroupMemberStatus memberStatus,
        LocalDateTime leftAt
) {
}
