package matchuri.backend.groupdecision.event;

import java.time.LocalDateTime;
import java.util.List;

public record GroupDeletedEvent(
        Long groupId,
        Long deletedByMemberId,
        List<Long> targetMemberIds,
        LocalDateTime deletedAt
) {
}
