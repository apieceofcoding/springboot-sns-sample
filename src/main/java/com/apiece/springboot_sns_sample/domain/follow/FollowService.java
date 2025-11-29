package com.apiece.springboot_sns_sample.domain.follow;

import com.apiece.springboot_sns_sample.domain.user.User;
import com.apiece.springboot_sns_sample.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserService userService;

    public Follow follow(User follower, Long followeeId) {
        User followee = userService.getById(followeeId);

        if (follower.getId().equals(followeeId)) {
            throw new IllegalArgumentException("Cannot follow yourself");
        }

        if (followRepository.existsByFollowerAndFolloweeAndDeletedAtIsNull(follower, followee)) {
            throw new IllegalStateException("Already following this user");
        }

        Follow follow = Follow.create(follower, followee);
        return followRepository.save(follow);
    }

    @Transactional
    public void unfollow(User follower, Long followeeId) {
        User followee = userService.getById(followeeId);

        Follow follow = followRepository.findByFollowerAndFolloweeAndDeletedAtIsNull(follower, followee)
                .orElseThrow(() -> new IllegalStateException("Not following this user"));

        follow.delete();
    }

    public List<Follow> getFollowers(User user) {
        return followRepository.findByFolloweeAndDeletedAtIsNull(user);
    }

    public List<Follow> getFollowees(User user) {
        return followRepository.findByFollowerAndDeletedAtIsNull(user);
    }

    public long getFollowersCount(User user) {
        return followRepository.countByFolloweeAndDeletedAtIsNull(user);
    }

    public long getFolloweesCount(User user) {
        return followRepository.countByFollowerAndDeletedAtIsNull(user);
    }
}
