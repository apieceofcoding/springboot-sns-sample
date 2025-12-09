package com.apiece.springboot_sns_sample.api;

import com.apiece.springboot_sns_sample.api.post.PostResponse;
import com.apiece.springboot_sns_sample.config.auth.AuthUser;
import com.apiece.springboot_sns_sample.domain.post.Post;
import com.apiece.springboot_sns_sample.domain.post.PostService;
import com.apiece.springboot_sns_sample.domain.timeline.TimelineService;
import com.apiece.springboot_sns_sample.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TimelineController {

    private final TimelineService timelineService;
    private final PostService postService;

    @GetMapping("/api/v1/timelines")
    public List<PostResponse> getTimeline(
            @AuthUser User user,
            @RequestParam(defaultValue = "50") int limit
    ) {
        List<Post> posts = timelineService.getTimeline(user, limit);
        return postService.enrichWithUserContext(posts, user).stream()
                .map(PostResponse::from)
                .toList();
    }
}
