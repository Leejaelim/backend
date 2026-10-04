package matchuri.backend.media.repository;

import java.util.Optional;
import matchuri.backend.media.entity.ImageAsset;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageAssetRepository extends JpaRepository<ImageAsset, Long> {

    Optional<ImageAsset> findByObjectKey(String objectKey);
}
