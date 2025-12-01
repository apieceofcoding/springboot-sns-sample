package com.apiece.springboot_sns_sample.api;

import com.apiece.springboot_sns_sample.api.post.PostResponse;
import com.apiece.springboot_sns_sample.api.reply.ReplyResponse;
import com.apiece.springboot_sns_sample.config.auth.AuthUser;
import com.apiece.springboot_sns_sample.domain.post.PostService;
import com.apiece.springboot_sns_sample.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final PostService postService;

    @GetMapping("/api/v1/profile/posts")
    public List<PostResponse> getMyPosts(@AuthUser User user) {
        return postService.getPostsByUserId(user.getId()).stream()
                .map(PostResponse::from)
                .toList();
    }

    @GetMapping("/api/v1/profile/replies")
    public List<ReplyResponse> getMyReplies(@AuthUser User user) {
        return postService.getRepliesByUserId(user.getId()).stream()
                .map(ReplyResponse::from)
                .toList();
    }
}
