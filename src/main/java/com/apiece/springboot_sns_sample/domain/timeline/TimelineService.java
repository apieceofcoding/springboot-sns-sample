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
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

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
        follows.parallelStream()
                .forEach(follow -> timelineRepository.addPostToTimeline(follow.getFollowerId(), postId));
    }

    public TimelinePage     getTimeline(User user, Double cursor, int limit) {
        // Fanout on Read for following celebs (addIfAbsent로 중복 방지)
        List<Follow> follows = followRepository.findByFollowerIdAndDeletedAtIsNull(user.getId());
        follows.parallelStream()
                .map(Follow::getFolloweeId)
                .map(followCountService::getFollowCount)
                .filter(FollowCount::isCeleb)
                .flatMap(followCount -> timelineRepository.getCelebPosts(followCount.getUserId(), 5).stream())
                .forEach(postId -> timelineRepository.addPostToTimelineIfAbsent(user.getId(), postId));

        List<TimelineEntry> entries = timelineRepository.getTimeline(user.getId(), cursor, limit);

        if (entries.isEmpty()) {
            return new TimelinePage(List.of(), null, false);
        }

        List<Long> postIds = entries.stream().map(TimelineEntry::postId).toList();
        Map<Long, Post> postMap = postRepository.findAllByIdInAndDeletedAtIsNull(postIds).stream()
                .collect(toMap(Post::getId, Function.identity()));

        // entries 순서대로 Post를 가져옴 (score 순서 유지)
        List<Post> posts = entries.stream()
                .map(entry -> postMap.get(entry.postId()))
                .filter(Objects::nonNull)
                .toList();

        // 다음 cursor는 마지막 항목의 score
        Double nextCursor = entries.getLast().score();
        boolean hasMore = entries.size() >= limit;

        return new TimelinePage(posts, nextCursor, hasMore);
    }
}
