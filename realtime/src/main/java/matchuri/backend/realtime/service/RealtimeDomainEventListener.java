package matchuri.backend.realtime.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import matchuri.backend.groupdecision.api.GroupAudienceQuery;
import matchuri.backend.realtime.entity.RealtimeEventType;
import matchuri.backend.groupdecision.event.GroupDeletedEvent;
import matchuri.backend.groupdecision.event.GroupInviteCreatedEvent;
import matchuri.backend.groupdecision.event.GroupMemberJoinedEvent;
import matchuri.backend.groupdecision.event.GroupMemberLeftEvent;
import matchuri.backend.groupdecision.event.GroupRecommendationFinalizedEvent;
import matchuri.backend.groupdecision.event.GroupRecommendationOpenedEvent;
import matchuri.backend.groupdecision.event.GroupRecommendationReadinessUpdatedEvent;
import matchuri.backend.groupdecision.event.GroupRecommendationStartedEvent;
import matchuri.backend.groupdecision.event.GroupRecommendationVoteCompletedEvent;
import matchuri.backend.groupdecision.event.GroupRecommendationVoteUpdatedEvent;
import matchuri.backend.realtime.result.GroupDeletedRealtimePayload;
import matchuri.backend.realtime.result.GroupInviteCreatedRealtimePayload;
import matchuri.backend.realtime.result.GroupMemberJoinedRealtimePayload;
import matchuri.backend.realtime.result.GroupMemberLeftRealtimePayload;
import matchuri.backend.realtime.result.GroupRecommendationFinalizedRealtimePayload;
import matchuri.backend.realtime.result.GroupRecommendationOpenedRealtimePayload;
import matchuri.backend.realtime.result.GroupRecommendationReadinessUpdatedRealtimePayload;
import matchuri.backend.realtime.result.GroupRecommendationStartedRealtimePayload;
import matchuri.backend.realtime.result.GroupRecommendationVoteCompletedRealtimePayload;
import matchuri.backend.realtime.result.GroupRecommendationVoteUpdatedRealtimePayload;
import matchuri.backend.realtime.result.RealtimeCandidatePayload;
import matchuri.backend.realtime.result.RealtimeReadinessProgressPayload;
import matchuri.backend.realtime.result.RealtimeVoteProgressPayload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class RealtimeDomainEventListener {

    private final RealtimeEventService realtimeEventService;
    private final GroupAudienceQuery audienceQuery;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(GroupInviteCreatedEvent event) {
        if (!audienceQuery.isPendingInvite(event.inviteId(), event.targetMemberId())) {
            return;
        }
        realtimeEventService.sendToMember(
                event.targetMemberId(),
                RealtimeEventType.GROUP_INVITE_CREATED,
                event.groupId(),
                null,
                event.requestMemberId(),
                new GroupInviteCreatedRealtimePayload(
                        event.inviteId(),
                        event.groupId(),
                        event.groupName(),
                        event.requestMemberId(),
                        event.requestMemberNickname(),
                        event.expiresAt()
                )
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(GroupMemberJoinedEvent event) {
        realtimeEventService.sendToGroupMembers(
                event.groupId(),
                activeMemberIds(event.groupId()),
                RealtimeEventType.GROUP_MEMBER_JOINED,
                event.groupId(),
                null,
                event.memberId(),
                new GroupMemberJoinedRealtimePayload(
                        event.groupId(),
                        event.memberId(),
                        event.memberNickname(),
                        event.joinedAt()
                )
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(GroupMemberLeftEvent event) {
        realtimeEventService.sendToGroupMembers(
                event.groupId(),
                activeMemberIds(event.groupId()),
                RealtimeEventType.GROUP_MEMBER_LEFT,
                event.groupId(),
                null,
                event.memberId(),
                new GroupMemberLeftRealtimePayload(
                        event.groupId(),
                        event.memberId(),
                        event.memberNickname(),
                        event.leftAt()
                )
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(GroupDeletedEvent event) {
        realtimeEventService.sendGroupDeletedToMembers(
                event.groupId(),
                event.targetMemberIds(),
                RealtimeEventType.GROUP_DELETED,
                event.groupId(),
                null,
                event.deletedByMemberId(),
                new GroupDeletedRealtimePayload(
                        event.groupId(),
                        event.deletedByMemberId(),
                        event.deletedAt()
                )
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(GroupRecommendationStartedEvent event) {
        realtimeEventService.sendToGroupMembers(
                event.groupId(),
                activeMemberIds(event.groupId()),
                RealtimeEventType.GROUP_RECOMMENDATION_STARTED,
                event.groupId(),
                event.sessionId(),
                event.actorMemberId(),
                new GroupRecommendationStartedRealtimePayload(
                        event.sessionId(),
                        event.status(),
                        RealtimeReadinessProgressPayload.from(event.readinessProgress())
                )
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(GroupRecommendationReadinessUpdatedEvent event) {
        realtimeEventService.sendToGroupMembers(
                event.groupId(),
                activeMemberIds(event.groupId()),
                RealtimeEventType.GROUP_RECOMMENDATION_READINESS_UPDATED,
                event.groupId(),
                event.sessionId(),
                event.readyMemberId(),
                new GroupRecommendationReadinessUpdatedRealtimePayload(
                        event.sessionId(),
                        event.status(),
                        event.readyMemberId(),
                        event.readyMemberNickname(),
                        RealtimeReadinessProgressPayload.from(event.readinessProgress())
                )
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(GroupRecommendationOpenedEvent event) {
        realtimeEventService.sendToGroupMembers(
                event.groupId(),
                activeMemberIds(event.groupId()),
                RealtimeEventType.GROUP_RECOMMENDATION_OPENED,
                event.groupId(),
                event.sessionId(),
                null,
                new GroupRecommendationOpenedRealtimePayload(
                        event.sessionId(),
                        event.status(),
                        event.candidates().stream()
                                .map(RealtimeCandidatePayload::from)
                                .toList(),
                        RealtimeVoteProgressPayload.from(event.voteProgress())
                )
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(GroupRecommendationVoteUpdatedEvent event) {
        realtimeEventService.sendToGroupMembers(
                event.groupId(),
                activeMemberIds(event.groupId()),
                RealtimeEventType.GROUP_RECOMMENDATION_VOTE_UPDATED,
                event.groupId(),
                event.sessionId(),
                null,
                new GroupRecommendationVoteUpdatedRealtimePayload(
                        event.sessionId(),
                        RealtimeVoteProgressPayload.from(event.voteProgress())
                )
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(GroupRecommendationVoteCompletedEvent event) {
        if (!audienceQuery.isActiveOwner(event.groupId(), event.ownerMemberId())) {
            return;
        }
        realtimeEventService.sendToMember(
                event.ownerMemberId(),
                RealtimeEventType.GROUP_RECOMMENDATION_VOTE_COMPLETED,
                event.groupId(),
                event.sessionId(),
                null,
                new GroupRecommendationVoteCompletedRealtimePayload(
                        event.sessionId(),
                        RealtimeVoteProgressPayload.from(event.voteProgress()),
                        true
                )
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(GroupRecommendationFinalizedEvent event) {
        realtimeEventService.sendToGroupMembers(
                event.groupId(),
                activeMemberIds(event.groupId()),
                RealtimeEventType.GROUP_RECOMMENDATION_FINALIZED,
                event.groupId(),
                event.sessionId(),
                event.actorMemberId(),
                new GroupRecommendationFinalizedRealtimePayload(
                        event.sessionId(),
                        event.status(),
                        event.finalizedAt(),
                        RealtimeCandidatePayload.from(event.finalCandidate())
                )
        );
    }

    private List<Long> activeMemberIds(Long groupId) {
        return audienceQuery.activeMemberIds(groupId);
    }
}
