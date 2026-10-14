package matchuri.backend.groupdecision.result;

import matchuri.backend.groupdecision.entity.GroupInvite;

public record GroupInviteV2SummaryResult(
        Long id,
        String groupName,
        String requestMemberProfileImageUrl,
        String requestMemberNickname
) {
    public static GroupInviteV2SummaryResult from(GroupInvite invite, String groupName, String requestMemberProfileImageUrl) {
        return new GroupInviteV2SummaryResult(
                invite.getId(),
                groupName,
                requestMemberProfileImageUrl,
                invite.getRequestMember().getNickname()
        );
    }
}
