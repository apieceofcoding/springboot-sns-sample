package com.apiece.springboot_sns_sample.api.post;

import com.apiece.springboot_sns_sample.domain.post.Post;

import java.time.LocalDateTime;

public record PostResponse(
        Long id,
        String content,
        Long userId,
        String username,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {
    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                post.getContent(),
                post.getUser().getId(),
                post.getUser().getUsername(),
                post.getCreatedAt(),
                post.getModifiedAt()
        );
    }
}
