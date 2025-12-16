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
        Integer quoteCount,
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
        Long repostIdByMe,
        // 관련 게시물 정보
        PostResponse repostedPost,
        PostResponse quotedPost,
        PostResponse parentPost,
        RepostedBy repostedBy
) {
    public record RepostedBy(Long userId, String username) {}

    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                post.getContent(),
                post.getUser().getId(),
                post.getUser().getUsername(),
                post.getRepostCount(),
                post.getLikeCount(),
                post.getReplyCount(),
                0, // quoteCount - TODO: add to Post entity if needed
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
                null,
                null,
                null,
                null,
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
                0,
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
                null,
                null,
                null,
                null,
                null
        );
    }

    public static PostResponse from(PostWithUserContext ctx) {
        Post post = ctx.post();

        // 재게시인 경우, repostedPost와 repostedBy 설정
        PostResponse repostedPostResponse = null;
        RepostedBy repostedBy = null;
        if (ctx.repostedPost() != null) {
            repostedPostResponse = fromSimple(ctx.repostedPost());
            repostedBy = new RepostedBy(
                    ctx.repostedByUserId() != null ? ctx.repostedByUserId() : post.getUser().getId(),
                    ctx.repostedByUsername() != null ? ctx.repostedByUsername() : post.getUser().getUsername()
            );
        }

        // 인용인 경우, quotedPost 설정
        PostResponse quotedPostResponse = null;
        if (ctx.quotedPost() != null) {
            quotedPostResponse = fromSimple(ctx.quotedPost());
        }

        // 답글인 경우, parentPost 설정
        PostResponse parentPostResponse = null;
        if (ctx.parentPost() != null) {
            parentPostResponse = fromSimple(ctx.parentPost());
        }

        return new PostResponse(
                post.getId(),
                post.getContent(),
                post.getUser().getId(),
                post.getUser().getUsername(),
                post.getRepostCount(),
                post.getLikeCount(),
                post.getReplyCount(),
                0,
                ctx.viewCount(),
                post.getMediaIds(),
                post.getParentId(),
                post.getQuoteId(),
                post.getRepostId(),
                post.getCreatedAt(),
                post.getModifiedAt(),
                ctx.isLikedByMe(),
                ctx.likeIdByMe(),
                ctx.isRepostedByMe(),
                ctx.repostIdByMe(),
                repostedPostResponse,
                quotedPostResponse,
                parentPostResponse,
                repostedBy
        );
    }

    // 중첩 게시물용 간단한 변환 (무한 재귀 방지)
    private static PostResponse fromSimple(Post post) {
        return new PostResponse(
                post.getId(),
                post.getContent(),
                post.getUser().getId(),
                post.getUser().getUsername(),
                post.getRepostCount(),
                post.getLikeCount(),
                post.getReplyCount(),
                0,
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
                null,
                null,
                null,
                null,
                null
        );
    }
}
