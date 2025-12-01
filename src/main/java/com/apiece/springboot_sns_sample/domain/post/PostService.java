package com.apiece.springboot_sns_sample.domain.post;

import com.apiece.springboot_sns_sample.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public Post createPost(String content, User user) {
        Post post = Post.create(content, user);
        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAllByDeletedAtIsNullOrderByCreatedAtDesc();
    }

    public Post getPostById(Long id) {
        return postRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found: " + id));
    }

    @Transactional
    public Post updatePost(Long id, String content, User user) {
        Post post = getPostById(id);

        if (!post.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You are not authorized to update this post");
        }

        if (post.isEditExpired()) {
            throw new IllegalArgumentException("Post can only be edited within 1 hour of creation");
        }

        post.updateContent(content);
        return post;
    }

    public void deletePost(Long id, User user) {
        Post post = getPostById(id);

        if (!post.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You are not authorized to delete this post");
        }

        post.delete();
        postRepository.save(post);
    }

    public List<Post> getPostsByUserId(Long userId) {
        return postRepository.findByUserIdAndParentIsNullAndDeletedAtIsNullOrderByCreatedAtDesc(userId);
    }

    public List<Post> getRepliesByUserId(Long userId) {
        return postRepository.findByUserIdAndParentIsNotNullAndDeletedAtIsNullOrderByCreatedAtDesc(userId);
    }
}
