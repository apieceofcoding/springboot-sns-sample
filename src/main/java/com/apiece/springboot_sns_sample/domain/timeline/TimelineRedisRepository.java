package com.apiece.springboot_sns_sample.domain.timeline;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TimelineRedisRepository implements TimelineRepository {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String TIMELINE_KEY_PREFIX = "timeline:user:";
    private static final String CELEB_POSTS_KEY_PREFIX = "timeline:fanout:celeb:";
    private static final int MAX_CELEB_POSTS_SIZE = 5;

    @Override
    public void addPostToTimeline(Long userId, Long postId) {
        String key = TIMELINE_KEY_PREFIX + userId;
        log.info("LPUSH {} {}", key, postId);
        redisTemplate.opsForList().leftPush(key, postId.toString());
    }

    @Override
    public List<Long> getTimeline(Long userId, int limit) {
        String key = TIMELINE_KEY_PREFIX + userId;
        log.info("LRANGE {} 0 {}", key, limit - 1);
        List<String> postIdStrings = redisTemplate.opsForList().range(key, 0, limit - 1);
        if (postIdStrings == null) return List.of();
        return postIdStrings.stream()
                .map(Long::parseLong)
                .toList();
    }

    @Override
    public void addCelebPost(Long celebUserId, Long postId) {
        String key = CELEB_POSTS_KEY_PREFIX + celebUserId;
        log.info("LPUSH {} {}", key, postId);
        redisTemplate.opsForList().leftPush(key, postId.toString());
        log.info("LTRIM {} 0 {}", key, MAX_CELEB_POSTS_SIZE - 1);
        redisTemplate.opsForList().trim(key, 0, MAX_CELEB_POSTS_SIZE - 1);
    }

    @Override
    public List<Long> getCelebPosts(Long celebUserId, int limit) {
        String key = CELEB_POSTS_KEY_PREFIX + celebUserId;
        int actualLimit = Math.min(limit, MAX_CELEB_POSTS_SIZE);
        log.info("LRANGE {} 0 {}", key, actualLimit - 1);
        List<String> postIdStrings = redisTemplate.opsForList().range(key, 0, actualLimit - 1);
        if (postIdStrings == null) return List.of();
        return postIdStrings.stream()
                .map(Long::parseLong)
                .toList();
    }
}
