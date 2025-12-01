package com.apiece.springboot_sns_sample.api.quote;

import com.apiece.springboot_sns_sample.domain.post.Post;

import java.time.LocalDateTime;

public record QuoteResponse(
        Long id,
        String content,
        Long userId,
        String username,
        Long quoteId,
        String quoteContent,
        Long quoteUserId,
        String quoteUsername,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {
    public static QuoteResponse from(Post post) {
        Post quotedPost = post.getQuote();
        return new QuoteResponse(
                post.getId(),
                post.getContent(),
                post.getUser().getId(),
                post.getUser().getUsername(),
                quotedPost != null ? quotedPost.getId() : null,
                quotedPost != null ? quotedPost.getContent() : null,
                quotedPost != null ? quotedPost.getUser().getId() : null,
                quotedPost != null ? quotedPost.getUser().getUsername() : null,
                post.getCreatedAt(),
                post.getModifiedAt()
        );
    }
}
