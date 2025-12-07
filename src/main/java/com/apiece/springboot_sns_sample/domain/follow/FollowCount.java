package com.apiece.springboot_sns_sample.domain.follow;

import com.apiece.springboot_sns_sample.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "follow_counts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FollowCount extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(name = "followers_count", nullable = false)
    private Long followersCount;

    @Column(name = "followees_count", nullable = false)
    private Long followeesCount;

    public static FollowCount create(Long userId) {
        FollowCount followCount = new FollowCount();
        followCount.userId = userId;
        followCount.followersCount = 0L;
        followCount.followeesCount = 0L;
        return followCount;
    }

    public void incrementFollowersCount() {
        this.followersCount++;
    }

    public void decrementFollowersCount() {
        if (this.followersCount > 0) {
            this.followersCount--;
        }
    }

    public void incrementFolloweesCount() {
        this.followeesCount++;
    }

    public void decrementFolloweesCount() {
        if (this.followeesCount > 0) {
            this.followeesCount--;
        }
    }

    public boolean isCeleb() {
        return this.followersCount >= 10000;
    }
}
