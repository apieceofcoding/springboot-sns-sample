package com.apiece.springboot_sns_sample.api.repost;

import com.apiece.springboot_sns_sample.domain.repost.Repost;

import java.time.LocalDateTime;

public record RepostResponse(
        Long id,
        Long userId,
        String username,
        Long postId,
        String postContent,
        LocalDateTime createdAt
) {
    public static RepostResponse from(Repost repost) {
        return new RepostResponse(
                repost.getId(),
                repost.getUser().getId(),
                repost.getUser().getUsername(),
                repost.getPost().getId(),
                repost.getPost().getContent(),
                repost.getCreatedAt()
        );
    }
}
