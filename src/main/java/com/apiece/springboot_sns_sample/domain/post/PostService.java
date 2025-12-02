package com.apiece.springboot_sns_sample.domain.post;

import com.apiece.springboot_sns_sample.domain.postview.PostViewService;
import com.apiece.springboot_sns_sample.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostViewService postViewService;

    public Post createPost(String content, User user) {
        Post post = Post.create(content, user);
        return postRepository.save(post);
    }

    public List<PostWithViewCount> getAllPosts() {
        List<Post> posts = postRepository.findAllByDeletedAtIsNullOrderByCreatedAtDesc();
        return enrichWithViewCount(posts);
    }

    public Post getPostById(Long id) {
        return postRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found: " + id));
    }

    public PostWithViewCount getPostByIdWithPostViewIncrement(Long id) {
        Post post = getPostById(id);
        postViewService.incrementPostView(id);
        Long viewCount = postViewService.getPostView(id);
        return new PostWithViewCount(post, viewCount);
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

    public List<PostWithViewCount> getPostsByUserId(Long userId) {
        List<Post> posts = postRepository.findByUserIdAndParentIsNullAndDeletedAtIsNullOrderByCreatedAtDesc(userId);
        return enrichWithViewCount(posts);
    }

    public List<PostWithViewCount> getRepliesByUserId(Long userId) {
        List<Post> posts = postRepository.findByUserIdAndParentIsNotNullAndDeletedAtIsNullOrderByCreatedAtDesc(userId);
        return enrichWithViewCount(posts);
    }

    public PostWithViewCount enrichWithViewCount(Post post) {
        Long viewCount = postViewService.getPostView(post.getId());
        return new PostWithViewCount(post, viewCount);
    }

    public List<PostWithViewCount> enrichWithViewCount(List<Post> posts) {
        return posts.stream()
                .map(this::enrichWithViewCount)
                .toList();
    }
}
