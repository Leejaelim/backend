package matchuri.backend.identity.api;

import java.util.Collection;
import java.util.List;

public interface AccountStatusQuery {
    boolean isActiveMember(Long memberId);
    List<Long> activeMemberIds(Collection<Long> memberIds);
}
