package matchuri.backend.groupdecision.repository;

import java.time.LocalDateTime;
import matchuri.backend.groupdecision.entity.GroupInvite;
import matchuri.backend.groupdecision.entity.GroupInviteStatus;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GroupInviteRepositoryCustom {

    Page<GroupInvite> findMyInvites(Long targetMemberId, @Nullable GroupInviteStatus status, LocalDateTime now, Pageable pageable);
}
