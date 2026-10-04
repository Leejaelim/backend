package matchuri.backend.groupdecision.result;

import matchuri.backend.groupdecision.entity.GroupMemberRole;

public record GroupRecommendationReadinessMemberResult(
        Long memberId,
        String nickname,
        GroupMemberRole role,
        boolean ready
) {
}
