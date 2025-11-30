package com.apiece.springboot_sns_sample.domain.follow;

import com.apiece.springboot_sns_sample.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FollowCountService {

    private final FollowCountRepository followCountRepository;

    public FollowCount getOrCreateFollowCount(User user) {
        return followCountRepository.findByUserAndDeletedAtIsNull(user)
                .orElseGet(() -> {
                    FollowCount followCount = FollowCount.create(user);
                    return followCountRepository.save(followCount);
                });
    }

    public FollowCount getFollowCount(User user) {
        return followCountRepository.findByUserAndDeletedAtIsNull(user)
                .orElseThrow(() -> new IllegalStateException("FollowCount not found for user"));
    }

    @Transactional
    public void incrementFollowersCount(User user) {
        FollowCount followCount = getOrCreateFollowCount(user);
        followCount.incrementFollowersCount();
    }

    @Transactional
    public void decrementFollowersCount(User user) {
        FollowCount followCount = getOrCreateFollowCount(user);
        followCount.decrementFollowersCount();
    }

    @Transactional
    public void incrementFolloweesCount(User user) {
        FollowCount followCount = getOrCreateFollowCount(user);
        followCount.incrementFolloweesCount();
    }

    @Transactional
    public void decrementFolloweesCount(User user) {
        FollowCount followCount = getOrCreateFollowCount(user);
        followCount.decrementFolloweesCount();
    }
}
