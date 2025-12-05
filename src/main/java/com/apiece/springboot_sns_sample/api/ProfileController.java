package com.apiece.springboot_sns_sample.api;

import com.apiece.springboot_sns_sample.api.post.PostResponse;
import com.apiece.springboot_sns_sample.api.reply.ReplyResponse;
import com.apiece.springboot_sns_sample.config.auth.AuthUser;
import com.apiece.springboot_sns_sample.domain.like.LikeService;
import com.apiece.springboot_sns_sample.domain.post.PostService;
import com.apiece.springboot_sns_sample.domain.post.PostWithViewCount;
import com.apiece.springboot_sns_sample.domain.repost.RepostService;
import com.apiece.springboot_sns_sample.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final PostService postService;
    private final RepostService repostService;
    private final LikeService likeService;

    @GetMapping("/api/v1/profile/posts")
    public List<PostResponse> getMyPosts(@AuthUser User user) {
        List<PostWithViewCount> postsWithViewCount = new ArrayList<>(postService.getPostsByUserId(user.getId()));

        repostService.getRepostsByUserId(user.getId()).forEach(repost -> {
            PostWithViewCount postWithViewCount = postService.enrichWithViewCount(repost.getPost());
            postsWithViewCount.add(postWithViewCount);
        });

        return postsWithViewCount.stream()
                .sorted(Comparator.comparing(pwc -> pwc.post().getCreatedAt(), Comparator.reverseOrder()))
                .map(PostResponse::from)
                .toList();
    }

    @GetMapping("/api/v1/profile/replies")
    public List<ReplyResponse> getMyReplies(@AuthUser User user) {
        return postService.getRepliesByUserId(user.getId()).stream()
                .map(ReplyResponse::from)
                .toList();
    }

    @GetMapping("/api/v1/profile/likes")
    public List<PostResponse> getMyLikedPosts(@AuthUser User user) {
        return likeService.getLikesByUserId(user.getId()).stream()
                .map(like -> postService.enrichWithViewCount(like.getPost()))
                .map(PostResponse::from)
                .toList();
    }
}
