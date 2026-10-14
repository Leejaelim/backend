package matchuri.backend.media.api.query;

import java.util.List;
import java.util.Optional;
import matchuri.backend.media.entity.PresetProfileImage;

public interface PresetProfileImageQuery {
    List<PresetProfileImage> findAllActive();
    Optional<PresetProfileImage> findActiveById(Long id);
    List<PresetProfileImage> findActiveDefaults();
}
