package matchuri.backend.groupdecision.query;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import matchuri.backend.groupdecision.api.GroupAudienceQuery;
import matchuri.backend.groupdecision.entity.GroupRoomStatus;
import matchuri.backend.groupdecision.repository.GroupInviteRepository;
import matchuri.backend.groupdecision.repository.GroupRoomMemberRepository;
import matchuri.backend.groupdecision.repository.GroupRoomRepository;
import matchuri.backend.identity.member.entity.MemberStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
public class JpaGroupAudienceQuery implements GroupAudienceQuery {
    private final GroupRoomRepository rooms;
    private final GroupRoomMemberRepository memberships;
    private final GroupInviteRepository invites;

    @Override
    public List<Long> activeMemberIds(Long groupId) {
        if (rooms.findById(groupId).filter(room -> room.getStatus() != GroupRoomStatus.DELETED).isEmpty()) {
            return List.of();
        }
        return memberships.findActiveMembersByRoomId(groupId).stream()
                .map(membership -> membership.getMember().getId()).toList();
    }

    @Override
    public boolean hasActiveMembership(Long groupId, Long memberId) {
        return memberships.findActiveMembershipInNotDeletedRoom(groupId, memberId)
                .filter(membership -> membership.getMember().getStatus() == MemberStatus.ACTIVE).isPresent();
    }

    @Override
    public boolean isActiveOwner(Long groupId, Long memberId) {
        return memberships.findActiveMembershipInNotDeletedRoom(groupId, memberId)
                .filter(membership -> membership.isOwner() && membership.getMember().getStatus() == MemberStatus.ACTIVE)
                .isPresent();
    }

    @Override
    public boolean isPendingInvite(Long inviteId, Long targetMemberId) {
        return invites.findById(inviteId)
                .filter(invite -> invite.isPending() && !invite.isExpired(LocalDateTime.now())
                        && invite.getRoom().getStatus() != GroupRoomStatus.DELETED
                        && invite.getTargetMember().getId().equals(targetMemberId)).isPresent();
    }
}
