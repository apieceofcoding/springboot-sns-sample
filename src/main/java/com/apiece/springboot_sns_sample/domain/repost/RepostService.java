package com.apiece.springboot_sns_sample.domain.repost;

import com.apiece.springboot_sns_sample.domain.post.Post;
import com.apiece.springboot_sns_sample.domain.post.PostRepository;
import com.apiece.springboot_sns_sample.domain.timeline.TimelineService;
import com.apiece.springboot_sns_sample.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RepostService {

    private final PostRepository postRepository;
    private final TimelineService timelineService;

    @Transactional
    public Post createRepost(Long repostId, User user) {
        Post originalPost = postRepository.findByIdAndDeletedAtIsNull(repostId)
                .orElseThrow(() -> new IllegalArgumentException("Post for repost not found: " + repostId));

        if (postRepository.existsByUserIdAndRepostIdAndDeletedAtIsNull(user.getId(), repostId)) {
            throw new IllegalArgumentException("You have already reposted this post");
        }

        originalPost.incrementRepostCount();
        Post repost = Post.createRepost(user, repostId);
        Post savedRepost = postRepository.save(repost);

        timelineService.fanOutToFollowers(savedRepost.getId(), user);

        return savedRepost;
    }

    public List<Post> getAllReposts() {
        return postRepository.findAllByDeletedAtIsNullOrderByCreatedAtDesc().stream()
                .filter(post -> post.getRepostId() != null)
                .toList();
    }

    public Post getRepostById(Long id) {
        Post post = postRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new IllegalArgumentException("Repost not found: " + id));
        if (post.getRepostId() == null) {
            throw new IllegalArgumentException("This is not a repost");
        }
        return post;
    }

    @Transactional
    public void deleteRepost(Long id, User user) {
        Post repost = getRepostById(id);

        if (!repost.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You are not authorized to delete this repost");
        }

        Post originalPost = postRepository.findByIdAndDeletedAtIsNull(repost.getRepostId())
                .orElseThrow(() -> new IllegalArgumentException("Original post not found: " + repost.getRepostId()));
        originalPost.decrementRepostCount();
        repost.delete();
        postRepository.save(repost);
    }
}
