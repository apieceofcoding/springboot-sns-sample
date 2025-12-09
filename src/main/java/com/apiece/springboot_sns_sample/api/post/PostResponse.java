package com.apiece.springboot_sns_sample.api.post;

import com.apiece.springboot_sns_sample.domain.post.Post;
import com.apiece.springboot_sns_sample.domain.post.PostWithViewCount;
import com.apiece.springboot_sns_sample.domain.post.PostWithUserContext;

import java.time.LocalDateTime;
import java.util.List;

public record PostResponse(
        Long id,
        String content,
        Long userId,
        String username,
        Integer repostCount,
        Integer likeCount,
        Integer replyCount,
        Long viewCount,
        List<Long> mediaIds,
        Long parentId,
        Long quoteId,
        Long repostId,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        Boolean isLikedByMe,
        Long likeIdByMe,
        Boolean isRepostedByMe,
        Long repostIdByMe
) {
    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                post.getContent(),
                post.getUser().getId(),
                post.getUser().getUsername(),
                post.getRepostCount(),
                post.getLikeCount(),
                post.getReplyCount(),
                post.getViewCount(),
                post.getMediaIds(),
                post.getParentId(),
                post.getQuoteId(),
                post.getRepostId(),
                post.getCreatedAt(),
                post.getModifiedAt(),
                false,
                null,
                false,
                null
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
                post.getReplyCount(),
                postWithViewCount.viewCount(),
                post.getMediaIds(),
                post.getParentId(),
                post.getQuoteId(),
                post.getRepostId(),
                post.getCreatedAt(),
                post.getModifiedAt(),
                false,
                null,
                false,
                null
        );
    }

    public static PostResponse from(PostWithUserContext postWithUserContext) {
        Post post = postWithUserContext.post();
        return new PostResponse(
                post.getId(),
                post.getContent(),
                post.getUser().getId(),
                post.getUser().getUsername(),
                post.getRepostCount(),
                post.getLikeCount(),
                post.getReplyCount(),
                postWithUserContext.viewCount(),
                post.getMediaIds(),
                post.getParentId(),
                post.getQuoteId(),
                post.getRepostId(),
                post.getCreatedAt(),
                post.getModifiedAt(),
                postWithUserContext.isLikedByMe(),
                postWithUserContext.likeIdByMe(),
                postWithUserContext.isRepostedByMe(),
                postWithUserContext.repostIdByMe()
        );
    }
}
