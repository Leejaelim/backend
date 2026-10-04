package matchuri.backend.media.repository;

import matchuri.backend.media.entity.PresetProfileImage;
import matchuri.backend.media.api.query.PresetProfileImageQuery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PresetProfileImageRepository extends JpaRepository<PresetProfileImage, Long>, PresetProfileImageQuery,
        PresetProfileImageRepositoryCustom {
}
