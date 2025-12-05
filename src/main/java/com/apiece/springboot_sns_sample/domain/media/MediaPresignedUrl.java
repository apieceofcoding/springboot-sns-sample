package com.apiece.springboot_sns_sample.domain.media;

public record MediaPresignedUrl(
        Media media,
        String presignedUrl
) {
}
