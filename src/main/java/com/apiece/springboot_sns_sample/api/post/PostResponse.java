package com.apiece.springboot_sns_sample.api.post;

import com.apiece.springboot_sns_sample.domain.post.Post;
import com.apiece.springboot_sns_sample.domain.post.PostWithViewCount;

import java.time.LocalDateTime;

public record PostResponse(
        Long id,
        String content,
        Long userId,
        String username,
        Integer repostCount,
        Integer likeCount,
        Long viewCount,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {
    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                post.getContent(),
                post.getUser().getId(),
                post.getUser().getUsername(),
                post.getRepostCount(),
                post.getLikeCount(),
                post.getViewCount(),
                post.getCreatedAt(),
                post.getModifiedAt()
        );
    }

    public static PostResponse from(PostWithViewCount postWithViewCount) {
        Post post = postWithViewCount.getPost();
        return new PostResponse(
                post.getId(),
                post.getContent(),
                post.getUser().getId(),
                post.getUser().getUsername(),
                post.getRepostCount(),
                post.getLikeCount(),
                postWithViewCount.getViewCount(),
                post.getCreatedAt(),
                post.getModifiedAt()
        );
    }
}
