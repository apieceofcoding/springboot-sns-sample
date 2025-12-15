package com.apiece.springboot_sns_sample.domain.timeline;

import com.apiece.springboot_sns_sample.domain.follow.Follow;
import com.apiece.springboot_sns_sample.domain.follow.FollowCount;
import com.apiece.springboot_sns_sample.domain.follow.FollowCountService;
import com.apiece.springboot_sns_sample.domain.follow.FollowRepository;
import com.apiece.springboot_sns_sample.domain.post.Post;
import com.apiece.springboot_sns_sample.domain.post.PostRepository;
import com.apiece.springboot_sns_sample.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TimelineService {

    private final TimelineRepository timelineRepository;
    private final FollowRepository followRepository;
    private final FollowCountService followCountService;
    private final PostRepository postRepository;

    public void fanOutToFollowers(Long postId, User author) {
        FollowCount followCount = followCountService.getFollowCount(author.getId());

        timelineRepository.addPostToTimeline(author.getId(), postId);

        // If author is celeb, cache the post instead of fanout
        if (followCount.isCeleb()) {
            timelineRepository.addCelebPost(author.getId(), postId);
            return;
        }

        // Fanout on Write for non-celeb users
        List<Follow> follows = followRepository.findByFolloweeIdAndDeletedAtIsNull(author.getId());
        follows.forEach(follow -> {
            Long followerId = follow.getFollowerId();
            timelineRepository.addPostToTimeline(followerId, postId);
        });
    }

    public List<Post> getTimeline(User user, int limit) {
        // Fanout on Read for following celebs
        List<Follow> follows = followRepository.findByFollowerIdAndDeletedAtIsNull(user.getId());
        follows.stream()
                .map(Follow::getFolloweeId)
                .map(followCountService::getFollowCount)
                .filter(FollowCount::isCeleb)
                .flatMap(followCount -> timelineRepository.getCelebPosts(followCount.getUserId(), 5).stream())
                .forEach(postId -> timelineRepository.addPostToTimeline(user.getId(), postId));

        List<Long> timelinePostIds = timelineRepository.getTimeline(user.getId(), limit);

        if (timelinePostIds.isEmpty()) return List.of();

        return postRepository.findAllByIdInAndDeletedAtIsNull(timelinePostIds).stream()
                .distinct()
                .sorted((p1, p2) -> p2.getCreatedAt().compareTo(p1.getCreatedAt()))
                .limit(limit)
                .toList();
    }
}
