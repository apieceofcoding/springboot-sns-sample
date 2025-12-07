package com.apiece.springboot_sns_sample.domain.timeline;

import java.util.List;

public interface TimelineRepository {

    void addPostToTimeline(Long userId, Long postId);

    List<Long> getTimeline(Long userId, int limit);

    void addCelebPost(Long celebUserId, Long postId);

    List<Long> getCelebPosts(Long celebUserId, int limit);
}
