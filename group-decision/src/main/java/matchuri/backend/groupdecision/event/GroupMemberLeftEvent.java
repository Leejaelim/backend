package matchuri.backend.groupdecision.event;

import java.time.LocalDateTime;

public record GroupMemberLeftEvent(
        Long groupId,
        Long memberId,
        String memberNickname,
        LocalDateTime leftAt
) {
}
