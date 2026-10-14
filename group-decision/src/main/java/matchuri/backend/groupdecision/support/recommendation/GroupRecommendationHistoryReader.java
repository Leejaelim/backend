package matchuri.backend.groupdecision.support.recommendation;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import matchuri.backend.groupdecision.entity.GroupMenuActionType;
import matchuri.backend.groupdecision.repository.GroupMenuActionRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroupRecommendationHistoryReader {

    private static final long RECENT_GROUP_SKIPPED_MENU_EXCLUSION_HOURS = 24;

    private final GroupMenuActionRepository groupMenuActionRepository;

    public List<Long> recentlySkippedMenuIds(Long groupRoomId) {
        LocalDateTime threshold = LocalDateTime.now().minusHours(RECENT_GROUP_SKIPPED_MENU_EXCLUSION_HOURS);

        return groupMenuActionRepository.findMenuItemIdsByGroupRoomIdAndActionTypeAndCreatedAtAfter(
                groupRoomId,
                GroupMenuActionType.SKIP,
                threshold
        );
    }
}

