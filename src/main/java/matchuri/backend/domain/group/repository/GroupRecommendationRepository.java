package matchuri.backend.domain.group.repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import matchuri.backend.domain.group.entity.GroupRecommendation;
import matchuri.backend.domain.group.entity.GroupRecommendationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GroupRecommendationRepository extends JpaRepository<GroupRecommendation, Long> {

    @Query("""
            select recommendation from GroupRecommendation recommendation
            join fetch recommendation.room room
            left join fetch recommendation.selectedCandidate candidate
            left join fetch candidate.menuItem
            where room.status <> matchuri.backend.domain.group.entity.GroupRoomStatus.DELETED
              and exists (
                  select membership.id from GroupRoomMember membership
                  where membership.room = room and membership.member.id = :memberId
                    and membership.status = matchuri.backend.domain.group.entity.GroupMemberStatus.ACTIVE
              )
              and not exists (
                  select newer.id from GroupRecommendation newer
                  where newer.room = room
                    and (newer.startedAt > recommendation.startedAt
                      or (newer.startedAt = recommendation.startedAt and newer.id > recommendation.id))
              )
            order by recommendation.startedAt desc, recommendation.id desc
            """)
    List<GroupRecommendation> findLatestForActiveMember(@Param("memberId") Long memberId);

    boolean existsByRoomIdAndStatusInAndStartedAtAfter(
            Long roomId,
            Collection<GroupRecommendationStatus> statuses,
            LocalDateTime startedAt
    );

    Optional<GroupRecommendation> findByIdAndRoomId(Long id, Long roomId);

    Optional<GroupRecommendation> findFirstByRoomIdOrderByStartedAtDescIdDesc(Long roomId);

    Page<GroupRecommendation> findByRoomIdOrderByStartedAtDescIdDesc(Long roomId, Pageable pageable);

    List<GroupRecommendation> findByRoomIdInAndStatusInAndEndedAtIsNullAndStartedAtLessThanEqual(
            Collection<Long> roomIds,
            Collection<GroupRecommendationStatus> statuses,
            LocalDateTime startedAt
    );

}
