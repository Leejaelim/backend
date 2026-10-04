package matchuri.backend.media.api.storage;

public record UploadObjectCommand(
        String objectKey,
        String contentType,
        byte[] content
) {
}
