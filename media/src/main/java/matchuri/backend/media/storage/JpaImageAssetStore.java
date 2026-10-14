package matchuri.backend.media.storage;

import lombok.RequiredArgsConstructor;
import matchuri.backend.media.api.asset.ImageAssetStore;
import matchuri.backend.media.entity.ImageAsset;
import matchuri.backend.media.repository.ImageAssetRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class JpaImageAssetStore implements ImageAssetStore {

    private final ImageAssetRepository imageAssetRepository;

    @Override
    @Transactional
    public ImageAsset save(ImageAsset asset) {
        return imageAssetRepository.save(asset);
    }
}
