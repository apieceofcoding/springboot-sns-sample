package com.apiece.springboot_sns_sample.api.follow;

import com.apiece.springboot_sns_sample.domain.follow.Follow;

import java.time.LocalDateTime;

public record FollowResponse(
        Long id,
        Long followerId,
        String followerUsername,
        Long followeeId,
        String followeeUsername,
        LocalDateTime createdAt
) {
    public static FollowResponse from(Follow follow) {
        return new FollowResponse(
                follow.getId(),
                follow.getFollower().getId(),
                follow.getFollower().getUsername(),
                follow.getFollowee().getId(),
                follow.getFollowee().getUsername(),
                follow.getCreatedAt()
        );
    }
}
