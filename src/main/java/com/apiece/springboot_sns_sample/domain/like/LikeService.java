package com.apiece.springboot_sns_sample.domain.like;

import com.apiece.springboot_sns_sample.domain.post.Post;
import com.apiece.springboot_sns_sample.domain.post.PostService;
import com.apiece.springboot_sns_sample.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostService postService;

    @Transactional
    public Like createLike(Long postId, User user) {
        Post post = postService.getPostById(postId);

        if (likeRepository.existsByUserIdAndPostIdAndDeletedAtIsNull(user.getId(), postId)) {
            throw new IllegalArgumentException("You have already liked this post");
        }

        post.incrementLikeCount();
        Like like = Like.create(user, post);
        return likeRepository.save(like);
    }

    public List<Like> getAllLikes() {
        return likeRepository.findAllByDeletedAtIsNullOrderByCreatedAtDesc();
    }

    public Like getLikeById(Long id) {
        return likeRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new IllegalArgumentException("Like not found: " + id));
    }

    @Transactional
    public void deleteLike(Long id, User user) {
        Like like = getLikeById(id);

        if (!like.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You are not authorized to delete this like");
        }

        like.getPost().decrementLikeCount();
        like.delete();
        likeRepository.save(like);
    }

    public List<Like> getLikesByUserId(Long userId) {
        return likeRepository.findByUserIdAndDeletedAtIsNullOrderByCreatedAtDesc(userId);
    }
}
