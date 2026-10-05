package matchuri.backend.groupdecision.support.room;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import matchuri.backend.groupdecision.entity.GroupRecommendation;
import matchuri.backend.groupdecision.entity.GroupRecommendationStatus;
import matchuri.backend.groupdecision.entity.GroupRoom;
import matchuri.backend.groupdecision.entity.GroupRoomMember;
import matchuri.backend.groupdecision.event.GroupMemberLeftEvent;
import matchuri.backend.groupdecision.event.GroupRecommendationOpenedEvent;
import matchuri.backend.groupdecision.event.GroupRecommendationVoteCompletedEvent;
import matchuri.backend.groupdecision.event.GroupRecommendationVoteUpdatedEvent;
import matchuri.backend.groupdecision.repository.GroupRecommendationRepository;
import matchuri.backend.groupdecision.repository.GroupRecommendationVoteRepository;
import matchuri.backend.groupdecision.repository.GroupRoomMemberRepository;
import matchuri.backend.groupdecision.result.GroupRecommendationReadinessProgressResult;
import matchuri.backend.groupdecision.result.GroupVoteProgressResult;
import matchuri.backend.groupdecision.support.recommendation.GroupRecommendationExpirationManager;
import matchuri.backend.groupdecision.support.recommendation.GroupRecommendationOpeningManager;
import matchuri.backend.groupdecision.support.recommendation.GroupRecommendationResultAssembler;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroupMemberDepartureManager {

    private static final List<GroupRecommendationStatus> ACTIVE_STATUSES = List.of(
            GroupRecommendationStatus.PREPARING,
            GroupRecommendationStatus.OPEN
    );

    private final GroupRecommendationRepository recommendationRepository;
    private final GroupRecommendationVoteRepository voteRepository;
    private final GroupRoomMemberRepository membershipRepository;
    private final GroupRecommendationExpirationManager expirationManager;
    private final GroupRecommendationOpeningManager openingManager;
    private final GroupRecommendationResultAssembler resultAssembler;
    private final ApplicationEventPublisher eventPublisher;

    public void leave(GroupRoomMember membership, LocalDateTime leftAt) {
        GroupRoom room = membership.getRoom();
        Long memberId = membership.getMember().getId();
        membership.leave(leftAt);

        recommendationRepository.findFirstByRoomIdAndStatusInOrderByCreatedAtDescIdDesc(room.getId(), ACTIVE_STATUSES)
                .ifPresent(recommendation -> updateRecommendation(room, recommendation, memberId, leftAt));

        eventPublisher.publishEvent(new GroupMemberLeftEvent(
                room.getId(), memberId, membership.getMember().getNickname(), leftAt
        ));
    }

    private void updateRecommendation(
            GroupRoom room,
            GroupRecommendation recommendation,
            Long memberId,
            LocalDateTime leftAt
    ) {
        if (expirationManager.expireGroupRecommendationIfNeeded(recommendation, leftAt)) {
            return;
        }

        if (recommendation.getStatus() == GroupRecommendationStatus.PREPARING) {
            int totalMemberCount = membershipRepository.findActiveMembersByRoomId(room.getId()).size();
            GroupRecommendationReadinessProgressResult readiness = resultAssembler.readinessProgress(
                    recommendation.getId(), room.getId(), totalMemberCount
            );
            if (readiness.allReady()) {
                var candidates = openingManager.open(room, recommendation);
                eventPublisher.publishEvent(new GroupRecommendationOpenedEvent(
                        room.getId(), recommendation.getId(), recommendation.getStatus(), candidates,
                        resultAssembler.toVoteProgress(recommendation)
                ));
            }
            return;
        }

        voteRepository.deleteByGroupRecommendationIdAndMemberId(recommendation.getId(), memberId);
        GroupVoteProgressResult progress = resultAssembler.toVoteProgress(recommendation);
        eventPublisher.publishEvent(new GroupRecommendationVoteUpdatedEvent(
                room.getId(), recommendation.getId(), progress
        ));
        if (progress.totalMemberCount() > 0
                && progress.totalMemberCount().equals(progress.votedMemberCount())) {
            eventPublisher.publishEvent(new GroupRecommendationVoteCompletedEvent(
                    room.getId(), recommendation.getId(), room.getHostMember().getId(), progress
            ));
        }
    }
}
