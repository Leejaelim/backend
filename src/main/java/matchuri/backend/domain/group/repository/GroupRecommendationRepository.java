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

public interface GroupRecommendationRepository extends JpaRepository<GroupRecommendation, Long>,
        GroupRecommendationRepositoryCustom {

    boolean existsByRoomIdAndStatusInAndCreatedAtAfter(
            Long roomId,
            Collection<GroupRecommendationStatus> statuses,
            LocalDateTime createdAt
    );

    Optional<GroupRecommendation> findByIdAndRoomId(Long id, Long roomId);

    Optional<GroupRecommendation> findFirstByRoomIdOrderByCreatedAtDescIdDesc(Long roomId);

    Page<GroupRecommendation> findByRoomIdOrderByCreatedAtDescIdDesc(Long roomId, Pageable pageable);

    List<GroupRecommendation> findByRoomIdInAndStatusInAndEndedAtIsNullAndCreatedAtLessThanEqual(
            Collection<Long> roomIds,
            Collection<GroupRecommendationStatus> statuses,
            LocalDateTime createdAt
    );

}
