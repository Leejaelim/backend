package matchuri.backend.groupdecision.repository;

import java.util.Optional;
import matchuri.backend.groupdecision.entity.GroupLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupLocationRepository extends JpaRepository<GroupLocation, Long> {

    Optional<GroupLocation> findFirstByRoomIdOrderByCreatedAtDescIdDesc(Long roomId);
}
