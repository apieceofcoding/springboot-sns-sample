package com.apiece.springboot_sns_sample.domain.repost;

import com.apiece.springboot_sns_sample.domain.post.Post;
import com.apiece.springboot_sns_sample.domain.post.PostService;
import com.apiece.springboot_sns_sample.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RepostService {

    private final RepostRepository repostRepository;
    private final PostService postService;

    @Transactional
    public Repost createRepost(Long postId, User user) {
        Post post = postService.getPostById(postId);

        if (repostRepository.existsByUserIdAndPostIdAndDeletedAtIsNull(user.getId(), postId)) {
            throw new IllegalArgumentException("You have already reposted this post");
        }

        post.incrementRepostCount();
        Repost repost = Repost.create(user, post);
        return repostRepository.save(repost);
    }

    public List<Repost> getAllReposts() {
        return repostRepository.findAllByDeletedAtIsNullOrderByCreatedAtDesc();
    }

    public Repost getRepostById(Long id) {
        return repostRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new IllegalArgumentException("Repost not found: " + id));
    }

    @Transactional
    public void deleteRepost(Long id, User user) {
        Repost repost = getRepostById(id);

        if (!repost.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You are not authorized to delete this repost");
        }

        repost.getPost().decrementRepostCount();
        repost.delete();
        repostRepository.save(repost);
    }

    public List<Repost> getRepostsByUserId(Long userId) {
        return repostRepository.findByUserIdAndDeletedAtIsNullOrderByCreatedAtDesc(userId);
    }
}
