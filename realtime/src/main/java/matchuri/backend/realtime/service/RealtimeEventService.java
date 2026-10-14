package matchuri.backend.realtime.service;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import matchuri.backend.groupdecision.exception.GroupErrorCode;
import matchuri.backend.groupdecision.api.GroupAudienceQuery;
import matchuri.backend.identity.api.AccountStatusQuery;
import matchuri.backend.identity.member.entity.Member;
import matchuri.backend.identity.api.MemberReader;
import matchuri.backend.realtime.entity.RealtimeEventType;
import matchuri.backend.realtime.result.RealtimeConnectedPayload;
import matchuri.backend.realtime.result.RealtimeEventEnvelope;
import matchuri.backend.realtime.support.RealtimeSseEmitterRegistry;
import matchuri.backend.shared.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class RealtimeEventService {

    private final MemberReader memberReader;
    private final GroupAudienceQuery audienceQuery;
    private final AccountStatusQuery accountStatusQuery;
    private final RealtimeSseEmitterRegistry emitterRegistry;

    @Transactional(readOnly = true)
    public SseEmitter connectMemberStream(Long memberId) {
        Member member = memberReader.getActiveMember(memberId);
        SseEmitter emitter = emitterRegistry.registerMember(member.getId());

        sendConnected(emitter, member.getId(), null);

        return emitter;
    }

    @Transactional(readOnly = true)
    public SseEmitter connectGroupStream(Long memberId, Long groupId) {
        Member member = memberReader.getActiveMember(memberId);

        if (!audienceQuery.hasActiveMembership(groupId, member.getId())) {
            throw new BusinessException(GroupErrorCode.ACCESS_DENIED, groupId);
        }

        SseEmitter emitter = emitterRegistry.registerGroup(groupId, member.getId());

        sendConnected(emitter, member.getId(), groupId);

        return emitter;
    }

    public void sendToMember(
            Long memberId,
            RealtimeEventType eventType,
            Long groupId,
            Long sessionId,
            Long actorMemberId,
            Object payload
    ) {
        if (!accountStatusQuery.isActiveMember(memberId)) {
            emitterRegistry.completeMemberConnections(memberId);
            return;
        }
        RealtimeEventEnvelope envelope = envelope(eventType, groupId, sessionId, actorMemberId, payload);
        emitterRegistry.sendToMember(memberId, envelope.eventId(), eventType.name(), envelope);
    }

    public void sendToGroup(
            Long groupId,
            RealtimeEventType eventType,
            Long eventGroupId,
            Long sessionId,
            Long actorMemberId,
            Object payload
    ) {
        sendToGroupMembers(groupId, audienceQuery.activeMemberIds(groupId), eventType, eventGroupId,
                sessionId, actorMemberId, payload);
    }

    public void sendToGroupMembers(
            Long groupId,
            Iterable<Long> memberIds,
            RealtimeEventType eventType,
            Long eventGroupId,
            Long sessionId,
            Long actorMemberId,
            Object payload
    ) {
        java.util.List<Long> eligible = audienceQuery.activeMemberIds(groupId);
        emitterRegistry.retainGroupConnections(groupId, eligible);
        java.util.List<Long> targets = toList(memberIds).stream().filter(eligible::contains).toList();
        RealtimeEventEnvelope envelope = envelope(eventType, eventGroupId, sessionId, actorMemberId, payload);
        emitterRegistry.sendToGroupMembers(
                groupId,
                targets,
                envelope.eventId(),
                eventType.name(),
                envelope
        );
    }

    public void sendGroupDeletedToMembers(
            Long groupId, Iterable<Long> snapshotMemberIds, RealtimeEventType eventType,
            Long eventGroupId, Long sessionId, Long actorMemberId, Object payload
    ) {
        try {
            java.util.List<Long> eligible = accountStatusQuery.activeMemberIds(toList(snapshotMemberIds));
            RealtimeEventEnvelope envelope = envelope(eventType, eventGroupId, sessionId, actorMemberId, payload);
            emitterRegistry.sendToGroupMembers(groupId, eligible, envelope.eventId(), eventType.name(), envelope);
        } finally {
            emitterRegistry.completeGroupConnections(groupId);
        }
    }

    private java.util.List<Long> toList(Iterable<Long> memberIds) {
        java.util.ArrayList<Long> result = new java.util.ArrayList<>();
        memberIds.forEach(result::add);
        return result;
    }

    private void sendConnected(SseEmitter emitter, Long memberId, Long groupId) {
        RealtimeEventEnvelope envelope = envelope(
                RealtimeEventType.REALTIME_CONNECTED,
                groupId,
                null,
                memberId,
                new RealtimeConnectedPayload(memberId, groupId, LocalDateTime.now())
        );

        emitterRegistry.sendToEmitter(
                emitter,
                envelope.eventId(),
                RealtimeEventType.REALTIME_CONNECTED.name(),
                envelope
        );
    }

    private RealtimeEventEnvelope envelope(
            RealtimeEventType eventType,
            Long groupId,
            Long sessionId,
            Long actorMemberId,
            Object payload
    ) {
        return new RealtimeEventEnvelope(
                UUID.randomUUID().toString(),
                eventType,
                LocalDateTime.now(),
                groupId,
                sessionId,
                actorMemberId,
                payload
        );
    }
}
