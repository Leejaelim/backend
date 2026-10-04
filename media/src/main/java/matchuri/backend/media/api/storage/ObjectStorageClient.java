package matchuri.backend.media.api.storage;

public interface ObjectStorageClient {

    void upload(UploadObjectCommand command);

    void delete(String objectKey);
}
