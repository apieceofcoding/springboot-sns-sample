package com.apiece.springboot_sns_sample.domain.repost;

import com.apiece.springboot_sns_sample.domain.base.BaseEntity;
import com.apiece.springboot_sns_sample.domain.post.Post;
import com.apiece.springboot_sns_sample.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reposts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Repost extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Post post;

    public static Repost create(User user, Post post) {
        Repost repost = new Repost();
        repost.user = user;
        repost.post = post;
        return repost;
    }
}
