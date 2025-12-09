package com.apiece.springboot_sns_sample.domain.post;

import com.apiece.springboot_sns_sample.domain.base.BaseEntity;
import com.apiece.springboot_sns_sample.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "posts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "quote_id")
    private Long quoteId;

    @Column(name = "repost_id")
    private Long repostId;

    @Column(nullable = false)
    private Integer repostCount = 0;

    @Column(nullable = false)
    private Integer likeCount = 0;

    @Column(nullable = false)
    private Integer replyCount = 0;

    @Column(nullable = false)
    private Long viewCount = 0L;

    @Column(name = "media_ids", columnDefinition = "bigint[]")
    private List<Long> mediaIds;

    public static Post create(String content, User user, List<Long> mediaIds) {
        Post post = new Post();
        post.content = content;
        post.user = user;
        post.repostCount = 0;
        post.likeCount = 0;
        post.replyCount = 0;
        post.viewCount = 0L;
        post.mediaIds = mediaIds;
        return post;
    }

    public static Post createReply(String content, User user, Long parentId) {
        Post post = create(content, user, null);
        post.parentId = parentId;
        return post;
    }

    public static Post createQuote(String content, User user, Long quoteId) {
        Post post = create(content, user, null);
        post.quoteId = quoteId;
        return post;
    }

    public static Post createRepost(User user, Long repostId) {
        Post post = create("", user, null);
        post.repostId = repostId;
        return post;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public boolean isEditExpired() {
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        return this.getCreatedAt().isBefore(oneHourAgo);
    }

    public void incrementRepostCount() {
        this.repostCount++;
    }

    public void decrementRepostCount() {
        if (this.repostCount > 0) {
            this.repostCount--;
        }
    }

    public void incrementLikeCount() {
        this.likeCount++;
    }

    public void decrementLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }

    public void incrementReplyCount() {
        this.replyCount++;
    }

    public void decrementReplyCount() {
        if (this.replyCount > 0) {
            this.replyCount--;
        }
    }

    public void updateViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }
}
