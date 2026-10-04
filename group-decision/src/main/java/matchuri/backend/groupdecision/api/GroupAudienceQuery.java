package matchuri.backend.groupdecision.api;

import java.util.List;

public interface GroupAudienceQuery {
    List<Long> activeMemberIds(Long groupId);
    boolean hasActiveMembership(Long groupId, Long memberId);
    boolean isActiveOwner(Long groupId, Long memberId);
    boolean isPendingInvite(Long inviteId, Long targetMemberId);
}
