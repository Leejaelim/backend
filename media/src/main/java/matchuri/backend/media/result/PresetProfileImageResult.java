package matchuri.backend.media.result;

public record PresetProfileImageResult(
        Long id,
        Long imageAssetId,
        String imageUrl,
        String objectKey,
        String originalFilename,
        String contentType,
        long contentLength,
        int width,
        int height,
        boolean isDefault
) {
}
