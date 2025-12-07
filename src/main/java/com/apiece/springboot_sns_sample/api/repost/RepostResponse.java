package com.apiece.springboot_sns_sample.api.repost;

import com.apiece.springboot_sns_sample.domain.post.Post;

import java.time.LocalDateTime;

public record RepostResponse(
        Long id,
        Long userId,
        String username,
        Long repostId,
        LocalDateTime createdAt
) {
    public static RepostResponse from(Post repost) {
        return new RepostResponse(
                repost.getId(),
                repost.getUser().getId(),
                repost.getUser().getUsername(),
                repost.getRepostId(),
                repost.getCreatedAt()
        );
    }
}
