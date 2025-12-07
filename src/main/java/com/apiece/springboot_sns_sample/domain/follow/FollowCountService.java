package com.apiece.springboot_sns_sample.domain.follow;

import com.apiece.springboot_sns_sample.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FollowCountService {

    private final FollowCountRepository followCountRepository;

    public FollowCount getOrCreateFollowCount(Long userId) {
        return followCountRepository.findByUserIdAndDeletedAtIsNull(userId)
                .orElseGet(() -> {
                    FollowCount followCount = FollowCount.create(userId);
                    return followCountRepository.save(followCount);
                });
    }

    public FollowCount getFollowCount(User user) {
        return followCountRepository.findByUserIdAndDeletedAtIsNull(user.getId())
                .orElseThrow(() -> new IllegalStateException("FollowCount not found for user"));
    }

    public FollowCount getFollowCount(Long userId) {
        return followCountRepository.findByUserIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new IllegalStateException("FollowCount not found for user"));
    }

    @Transactional
    public void incrementFollowersCount(User user) {
        FollowCount followCount = getOrCreateFollowCount(user.getId());
        followCount.incrementFollowersCount();
    }

    @Transactional
    public void decrementFollowersCount(User user) {
        FollowCount followCount = getOrCreateFollowCount(user.getId());
        followCount.decrementFollowersCount();
    }

    @Transactional
    public void incrementFolloweesCount(User user) {
        FollowCount followCount = getOrCreateFollowCount(user.getId());
        followCount.incrementFolloweesCount();
    }

    @Transactional
    public void decrementFolloweesCount(User user) {
        FollowCount followCount = getOrCreateFollowCount(user.getId());
        followCount.decrementFolloweesCount();
    }
}
