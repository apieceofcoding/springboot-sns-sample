package com.apiece.springboot_sns_sample.domain.media;

import com.apiece.springboot_sns_sample.config.s3.RustFsProperties;
import com.apiece.springboot_sns_sample.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MediaService {

    private final MediaRepository mediaRepository;
    private final S3Presigner s3Presigner;
    private final RustFsProperties rustFsProperties;

    public MediaPresignedUrl initMedia(MediaType mediaType, User user) {
        String filename = UUID.randomUUID() + mediaType.fileExtension();
        String path = String.format("media/users/%s/%s", user.getId(), filename);

        // Generate presigned URL for upload
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(rustFsProperties.bucket())
                .key(path)
                .contentType(mediaType.contentType())
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofSeconds(rustFsProperties.presignedUrlExpirationSeconds()))
                .putObjectRequest(putObjectRequest)
                .build();

        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);
        String presignedUrl = presignedRequest.url().toString();

        Media media = Media.create(mediaType, path, user.getId());
        media = mediaRepository.save(media);

        return new MediaPresignedUrl(media, presignedUrl);
    }

    public Media getMediaById(Long id) {
        return mediaRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new IllegalArgumentException("Media not found: " + id));
    }

    public List<Media> getMediaByUserId(Long userId) {
        return mediaRepository.findByUserIdAndDeletedAtIsNullOrderByCreatedAtDesc(userId);
    }

    public void deleteMedia(Long id, User user) {
        Media media = getMediaById(id);

        if (!media.getUserId().equals(user.getId())) {
            throw new IllegalArgumentException("You are not authorized to delete this media");
        }

        media.delete();
        mediaRepository.save(media);
    }
}
