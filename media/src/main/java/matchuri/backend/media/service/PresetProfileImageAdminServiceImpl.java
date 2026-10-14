package matchuri.backend.media.service;

import matchuri.backend.media.api.PresetProfileImageAdminService;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matchuri.backend.media.entity.ImageAsset;
import matchuri.backend.media.entity.ImageStorageProvider;
import matchuri.backend.media.entity.PresetProfileImage;
import matchuri.backend.media.exception.ImageErrorCode;
import matchuri.backend.media.repository.ImageAssetRepository;
import matchuri.backend.media.repository.PresetProfileImageRepository;
import matchuri.backend.media.result.PresetProfileImageResult;
import matchuri.backend.media.support.ImageChecksumCalculator;
import matchuri.backend.media.support.ImageObjectKeyGenerator;
import matchuri.backend.media.support.ImageUploadValidator;
import matchuri.backend.media.support.ImageUploadValidator.ValidatedImage;
import matchuri.backend.media.support.ImageUrlResolver;
import matchuri.backend.media.event.PresetProfileImageDeleted;
import matchuri.backend.media.config.R2Config;
import matchuri.backend.shared.exception.BusinessException;
import matchuri.backend.media.api.storage.ObjectStorageClient;
import matchuri.backend.media.api.storage.UploadObjectCommand;
import org.springframework.stereotype.Service;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class PresetProfileImageAdminServiceImpl implements PresetProfileImageAdminService {

    private final PresetProfileImageRepository presetProfileImageRepository;
    private final ImageAssetRepository imageAssetRepository;
    private final ImageUploadValidator imageUploadValidator;
    private final ImageObjectKeyGenerator imageObjectKeyGenerator;
    private final ImageChecksumCalculator imageChecksumCalculator;
    private final ImageUrlResolver imageUrlResolver;
    private final ObjectStorageClient objectStorageClient;
    private final R2Config r2Config;
    private final TransactionTemplate transactionTemplate;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional(readOnly = true)
    public List<PresetProfileImageResult> getPresetProfileImages() {
        return presetProfileImageRepository.findAllActive().stream()
                .map(this::toResult)
                .toList();
    }

    @Override
    public PresetProfileImageResult upload(MultipartFile file) {
        ValidatedImage image = imageUploadValidator.validate(file);
        String objectKey = imageObjectKeyGenerator.presetProfileImageKey(image.contentType());

        objectStorageClient.upload(new UploadObjectCommand(objectKey, image.contentType(), image.bytes()));

        try {
            return transactionTemplate.execute(status -> saveUploadedImage(file, image, objectKey));
        } catch (RuntimeException exception) {
            deleteUploadedObjectAfterDbFailure(objectKey);
            throw exception;
        }
    }

    @Override
    @Transactional
    public PresetProfileImageResult setDefault(Long presetProfileImageId) {
        List<PresetProfileImage> presets = presetProfileImageRepository.lockAllActive();
        PresetProfileImage selected = presets.stream()
                .filter(preset -> preset.getId().equals(presetProfileImageId))
                .findFirst()
                .orElseThrow(() -> new BusinessException(
                        ImageErrorCode.PRESET_PROFILE_NOT_FOUND,
                        presetProfileImageId
                ));

        presets.forEach(PresetProfileImage::clearDefault);
        selected.setDefault();
        return toResult(selected);
    }

    @Override
    @Transactional
    public void delete(Long presetProfileImageId) {
        List<PresetProfileImage> presets = presetProfileImageRepository.lockAllActive();
        PresetProfileImage selected = presets.stream()
                .filter(preset -> preset.getId().equals(presetProfileImageId))
                .findFirst()
                .orElseThrow(() -> new BusinessException(
                        ImageErrorCode.PRESET_PROFILE_NOT_FOUND,
                        presetProfileImageId
                ));

        if (selected.isDefault()) {
            throw new BusinessException(ImageErrorCode.DEFAULT_PRESET_PROFILE_DELETE_NOT_ALLOWED);
        }

        selected.delete();
        updateAllToDefault(selected);
    }

    private void updateAllToDefault(PresetProfileImage deleted) {
        Long deletedAssetId = deleted.getImageAsset().getId();
        PresetProfileImage defaultPreset = presetProfileImageRepository.findActiveDefaults().getFirst();
        Long updateAssetId = defaultPreset.getImageAsset().getId();
        eventPublisher.publishEvent(new PresetProfileImageDeleted(deletedAssetId, updateAssetId));
    }

    private PresetProfileImageResult saveUploadedImage(
            MultipartFile file,
            ValidatedImage image,
            String objectKey
    ) {
        ImageAsset imageAsset = imageAssetRepository.save(new ImageAsset(
                ImageStorageProvider.CLOUDFLARE_R2,
                r2Config.getBucket(),
                objectKey,
                normalizeOriginalFilename(file.getOriginalFilename()),
                image.contentType(),
                image.bytes().length,
                imageChecksumCalculator.sha256Hex(image.bytes()),
                image.width(),
                image.height()
        ));
        PresetProfileImage preset = presetProfileImageRepository.save(
                new PresetProfileImage(imageAsset, false)
        );
        return toResult(preset);
    }

    private PresetProfileImageResult toResult(PresetProfileImage preset) {
        ImageAsset asset = preset.getImageAsset();
        return new PresetProfileImageResult(
                preset.getId(),
                asset.getId(),
                imageUrlResolver.toPublicUrl(asset.getObjectKey()),
                asset.getObjectKey(),
                asset.getOriginalFilename(),
                asset.getContentType(),
                asset.getContentLength(),
                asset.getWidth(),
                asset.getHeight(),
                preset.isDefault()
        );
    }

    private String normalizeOriginalFilename(String originalFilename) {
        if (originalFilename == null || originalFilename.isBlank()) {
            return null;
        }

        String trimmed = originalFilename.trim();
        if (trimmed.length() <= ImageAsset.ORIGINAL_FILENAME_MAX_LENGTH) {
            return trimmed;
        }

        return trimmed.substring(0, ImageAsset.ORIGINAL_FILENAME_MAX_LENGTH);
    }

    private void deleteUploadedObjectAfterDbFailure(String objectKey) {
        try {
            objectStorageClient.delete(objectKey);
        } catch (RuntimeException deleteException) {
            log.warn("Failed to delete uploaded preset profile image after DB failure. objectKey={}",
                    objectKey, deleteException);
        }
    }
}
