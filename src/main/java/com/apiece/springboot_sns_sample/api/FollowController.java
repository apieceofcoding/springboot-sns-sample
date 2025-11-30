package com.apiece.springboot_sns_sample.api;

import com.apiece.springboot_sns_sample.api.follow.FollowRequest;
import com.apiece.springboot_sns_sample.api.follow.FollowResponse;
import com.apiece.springboot_sns_sample.config.auth.AuthUser;
import com.apiece.springboot_sns_sample.domain.follow.Follow;
import com.apiece.springboot_sns_sample.domain.follow.FollowService;
import com.apiece.springboot_sns_sample.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @PostMapping("/api/v1/follows")
    public FollowResponse follow(
            @RequestBody FollowRequest request,
            @AuthUser User user
    ) {
        Follow follow = followService.follow(user, request.followeeId());
        return FollowResponse.from(follow);
    }

    @DeleteMapping("/api/v1/follows")
    public void unfollow(
            @RequestBody FollowRequest request,
            @AuthUser User user
    ) {
        followService.unfollow(user, request.followeeId());
    }

    @GetMapping("/api/v1/follows/followers")
    public List<FollowResponse> getFollowers(@AuthUser User user) {
        List<Follow> followers = followService.getFollowers(user);
        return followers.stream()
                .map(FollowResponse::from)
                .toList();
    }

    @GetMapping("/api/v1/follows/followees")
    public List<FollowResponse> getFollowees(@AuthUser User user) {
        List<Follow> followees = followService.getFollowees(user);
        return followees.stream()
                .map(FollowResponse::from)
                .toList();
    }
}
