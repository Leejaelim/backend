package matchuri.backend.groupdecision.repository;

import matchuri.backend.groupdecision.entity.GroupMenuAction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupMenuActionRepository extends JpaRepository<GroupMenuAction, Long>,
        GroupMenuActionRepositoryCustom {
}
