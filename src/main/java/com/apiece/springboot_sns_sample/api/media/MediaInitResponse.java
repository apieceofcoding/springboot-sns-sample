package com.apiece.springboot_sns_sample.api.media;

import com.apiece.springboot_sns_sample.domain.media.Media;
import com.apiece.springboot_sns_sample.domain.media.MediaStatus;
import com.apiece.springboot_sns_sample.domain.media.MediaType;

import java.time.LocalDateTime;
import java.util.Map;

public record MediaInitResponse(
        Long id,
        MediaType mediaType,
        String path,
        MediaStatus status,
        Long userId,
        String presignedUrl,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {
    public static MediaInitResponse from(Media media, String presignedUrl) {
        return new MediaInitResponse(
                media.getId(),
                media.getMediaType(),
                media.getPath(),
                media.getStatus(),
                media.getUserId(),
                presignedUrl,
                media.getCreatedAt(),
                media.getModifiedAt()
        );
    }
}
