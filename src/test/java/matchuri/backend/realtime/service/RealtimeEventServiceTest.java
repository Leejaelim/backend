package matchuri.backend.realtime.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import matchuri.backend.groupdecision.api.GroupAudienceQuery;
import matchuri.backend.identity.api.AccountStatusQuery;
import matchuri.backend.identity.api.MemberReader;
import matchuri.backend.realtime.entity.RealtimeEventType;
import matchuri.backend.realtime.support.RealtimeSseEmitterRegistry;
import org.junit.jupiter.api.Test;

class RealtimeEventServiceTest {
    private final GroupAudienceQuery groups = mock(GroupAudienceQuery.class);
    private final AccountStatusQuery accounts = mock(AccountStatusQuery.class);
    private final RealtimeSseEmitterRegistry registry = mock(RealtimeSseEmitterRegistry.class);
    private final RealtimeEventService service = new RealtimeEventService(mock(MemberReader.class), groups, accounts, registry);

    @Test
    void groupRecipientsMustStillHaveActiveMembership() {
        when(groups.activeMemberIds(10L)).thenReturn(List.of(1L, 3L));
        service.sendToGroupMembers(10L, List.of(1L, 2L), RealtimeEventType.GROUP_MEMBER_JOINED, 10L, null, 1L, "payload");
        verify(registry).retainGroupConnections(10L, List.of(1L, 3L));
        verify(registry).sendToGroupMembers(eq(10L), eq(List.of(1L)), anyString(), eq("GROUP_MEMBER_JOINED"), any());
    }

    @Test
    void inactiveAccountReceivesNoPersonalEventAndConnectionsClose() {
        service.sendToMember(2L, RealtimeEventType.GROUP_INVITE_CREATED, 10L, null, 1L, "payload");
        verify(registry).completeMemberConnections(2L);
        verify(registry, never()).sendToMember(any(), anyString(), anyString(), any());
    }

    @Test
    void groupDeletionUsesActiveAccountsInSnapshotThenClosesGroupStream() {
        when(accounts.activeMemberIds(List.of(1L, 2L))).thenReturn(List.of(1L));
        service.sendGroupDeletedToMembers(10L, List.of(1L, 2L), RealtimeEventType.GROUP_DELETED, 10L, null, 1L, "payload");
        var order = inOrder(registry);
        order.verify(registry).sendToGroupMembers(eq(10L), eq(List.of(1L)), anyString(), eq("GROUP_DELETED"), any());
        order.verify(registry).completeGroupConnections(10L);
        verifyNoInteractions(groups);
    }

    @Test
    void deletedGroupConnectionsCloseEvenIfRecipientLookupFails() {
        when(accounts.activeMemberIds(List.of(1L))).thenThrow(new IllegalStateException("lookup failed"));
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> service.sendGroupDeletedToMembers(
                10L, List.of(1L), RealtimeEventType.GROUP_DELETED, 10L, null, 1L, "payload"))
                .isInstanceOf(IllegalStateException.class);
        verify(registry).completeGroupConnections(10L);
    }
}
