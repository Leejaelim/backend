package matchuri.backend.media.api.asset;

import matchuri.backend.media.entity.ImageAsset;

public interface ImageAssetStore {
    ImageAsset save(ImageAsset asset);
}
