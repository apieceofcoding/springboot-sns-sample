package com.apiece.springboot_sns_sample.api.reply;

import com.apiece.springboot_sns_sample.domain.post.Post;

import java.time.LocalDateTime;

public record ReplyResponse(
        Long id,
        String content,
        Long userId,
        String username,
        Long parentId,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {
    public static ReplyResponse from(Post post) {
        return new ReplyResponse(
                post.getId(),
                post.getContent(),
                post.getUser().getId(),
                post.getUser().getUsername(),
                post.getParent() != null ? post.getParent().getId() : null,
                post.getCreatedAt(),
                post.getModifiedAt()
        );
    }
}
