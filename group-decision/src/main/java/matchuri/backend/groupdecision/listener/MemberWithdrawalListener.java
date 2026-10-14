package matchuri.backend.groupdecision.listener;

import lombok.RequiredArgsConstructor;
import matchuri.backend.groupdecision.repository.GroupRoomRepository;
import matchuri.backend.groupdecision.repository.GroupRoomMemberRepository;
import matchuri.backend.groupdecision.support.room.GroupMemberDepartureManager;
import matchuri.backend.identity.member.event.MemberWithdrawn;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class MemberWithdrawalListener {
    private final GroupRoomRepository repository;
    private final GroupRoomMemberRepository membershipRepository;
    private final GroupMemberDepartureManager departureManager;

    @EventListener
    @Transactional(propagation = Propagation.MANDATORY)
    public void on(MemberWithdrawn event) {
        repository.findOwnedNotDeletedForUpdate(event.memberId())
                .forEach(room -> room.delete(event.deletedAt()));
        membershipRepository.findActiveMembershipsInOtherRooms(event.memberId())
                .forEach(membership -> departureManager.leave(membership, event.deletedAt()));
    }
}
