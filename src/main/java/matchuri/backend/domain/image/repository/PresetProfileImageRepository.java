package matchuri.backend.domain.image.repository;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import matchuri.backend.domain.image.entity.PresetProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PresetProfileImageRepository extends JpaRepository<PresetProfileImage, Long> {

    @Query("""
            select preset
            from PresetProfileImage preset
            join fetch preset.imageAsset
            where preset.isDeleted = false
            order by preset.id
            """)
    List<PresetProfileImage> findAllActive();

    @Query("""
            select preset
            from PresetProfileImage preset
            join fetch preset.imageAsset
            where preset.id = :presetProfileImageId
              and preset.isDeleted = false
            """)
    Optional<PresetProfileImage> findActiveById(@Param("presetProfileImageId") Long presetProfileImageId);

    @Query("""
            select preset
            from PresetProfileImage preset
            join fetch preset.imageAsset
            where preset.isDefault = true
              and preset.isDeleted = false
            """)
    List<PresetProfileImage> findActiveDefaults();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select preset
            from PresetProfileImage preset
            join fetch preset.imageAsset
            where preset.isDeleted = false
            order by preset.id
            """)
    List<PresetProfileImage> lockAllActive();

    @Query("""
            select preset
            from PresetProfileImage preset
            join fetch preset.imageAsset
            where preset.imageAsset.objectKey = :objectKey
            """)
    Optional<PresetProfileImage> findByObjectKey(@Param("objectKey") String objectKey);
}
