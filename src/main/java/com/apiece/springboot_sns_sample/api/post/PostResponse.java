package com.apiece.springboot_sns_sample.api.post;

import com.apiece.springboot_sns_sample.domain.post.Post;
import com.apiece.springboot_sns_sample.domain.post.PostWithViewCount;

import java.time.LocalDateTime;
import java.util.List;

public record PostResponse(
        Long id,
        String content,
        Long userId,
        String username,
        Integer repostCount,
        Integer likeCount,
        Long viewCount,
        List<Long> mediaIds,
        Long parentId,
        Long quoteId,
        Long repostId,
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
                post.getMediaIds(),
                post.getParentId(),
                post.getQuoteId(),
                post.getRepostId(),
                post.getCreatedAt(),
                post.getModifiedAt()
        );
    }

    public static PostResponse from(PostWithViewCount postWithViewCount) {
        Post post = postWithViewCount.post();
        return new PostResponse(
                post.getId(),
                post.getContent(),
                post.getUser().getId(),
                post.getUser().getUsername(),
                post.getRepostCount(),
                post.getLikeCount(),
                postWithViewCount.viewCount(),
                post.getMediaIds(),
                post.getParentId(),
                post.getQuoteId(),
                post.getRepostId(),
                post.getCreatedAt(),
                post.getModifiedAt()
        );
    }
}
