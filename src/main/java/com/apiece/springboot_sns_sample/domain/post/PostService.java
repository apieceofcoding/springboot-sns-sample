package com.apiece.springboot_sns_sample.domain.post;

import com.apiece.springboot_sns_sample.domain.like.Like;
import com.apiece.springboot_sns_sample.domain.like.LikeRepository;
import com.apiece.springboot_sns_sample.domain.media.Media;
import com.apiece.springboot_sns_sample.domain.media.MediaRepository;
import com.apiece.springboot_sns_sample.domain.postview.PostViewService;
import com.apiece.springboot_sns_sample.domain.timeline.TimelineService;
import com.apiece.springboot_sns_sample.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostViewService postViewService;
    private final MediaRepository mediaRepository;
    private final TimelineService timelineService;
    private final LikeRepository likeRepository;

    public Post createPost(String content, List<Long> mediaIds, User user) {
        if (mediaIds != null && !mediaIds.isEmpty()) {
            List<Media> mediaList = mediaRepository.findAllById(mediaIds);

            if (mediaList.size() != mediaIds.size()) {
                throw new IllegalArgumentException("Some media not found");
            }

            mediaList.forEach(media -> {
                if (!media.getUserId().equals(user.getId())) {
                    throw new IllegalArgumentException("You are not authorized to use this media: " + media.getId());
                }
            });
        }

        Post post = Post.create(content, user, mediaIds);
        Post savedPost = postRepository.save(post);

        timelineService.fanOutToFollowers(savedPost.getId(), user);

        return savedPost;
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
        List<Post> posts = postRepository.findByUserIdAndParentIdIsNullAndDeletedAtIsNullOrderByCreatedAtDesc(userId);
        return enrichWithViewCount(posts);
    }

    public List<PostWithViewCount> getRepliesByUserId(Long userId) {
        List<Post> posts = postRepository.findByUserIdAndParentIdIsNotNullAndDeletedAtIsNullOrderByCreatedAtDesc(userId);
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

    public PostWithUserContext enrichWithUserContext(Post post, User user) {
        Long viewCount = postViewService.getPostView(post.getId());

        // Check if user liked this post
        var like = likeRepository.findByUserIdAndPostIdAndDeletedAtIsNull(user.getId(), post.getId());
        boolean isLikedByMe = like.isPresent();
        Long likeIdByMe = like.map(Like::getId).orElse(null);

        // Check if user reposted this post
        var repost = postRepository.findByUserIdAndRepostIdAndDeletedAtIsNull(user.getId(), post.getId());
        boolean isRepostedByMe = repost.isPresent();
        Long repostIdByMe = repost.map(Post::getId).orElse(null);

        // 관련 게시물 조회
        Post repostedPost = null;
        Post quotedPost = null;
        Post parentPost = null;
        Long repostedByUserId = null;
        String repostedByUsername = null;

        // 재게시인 경우 원본 게시물 조회
        if (post.getRepostId() != null) {
            repostedPost = postRepository.findByIdAndDeletedAtIsNull(post.getRepostId()).orElse(null);
        }

        // 인용인 경우 인용된 게시물 조회
        if (post.getQuoteId() != null) {
            quotedPost = postRepository.findByIdAndDeletedAtIsNull(post.getQuoteId()).orElse(null);
        }

        // 재게시 또는 인용한 사람 정보
        if (post.getRepostId() != null || post.getQuoteId() != null) {
            repostedByUserId = post.getUser().getId();
            repostedByUsername = post.getUser().getUsername();
        }

        // 답글인 경우 부모 게시물 조회
        if (post.getParentId() != null) {
            parentPost = postRepository.findByIdAndDeletedAtIsNull(post.getParentId()).orElse(null);
        }

        return new PostWithUserContext(
                post, viewCount, isLikedByMe, likeIdByMe, isRepostedByMe, repostIdByMe,
                repostedPost, quotedPost, parentPost, repostedByUserId, repostedByUsername
        );
    }

    public List<PostWithUserContext> enrichWithUserContext(List<Post> posts, User user) {
        if (posts.isEmpty()) {
            return List.of();
        }

        // 1. 모든 게시물의 ID 수집
        List<Long> postIds = posts.stream().map(Post::getId).toList();

        // 2. 관련 게시물 ID 수집 (repostId, quoteId, parentId)
        Set<Long> relatedPostIds = new HashSet<>();
        for (Post post : posts) {
            if (post.getRepostId() != null) relatedPostIds.add(post.getRepostId());
            if (post.getQuoteId() != null) relatedPostIds.add(post.getQuoteId());
            if (post.getParentId() != null) relatedPostIds.add(post.getParentId());
        }

        // 3. 관련 게시물 일괄 조회
        Map<Long, Post> relatedPostsMap = new HashMap<>();
        if (!relatedPostIds.isEmpty()) {
            List<Post> relatedPosts = postRepository.findAllByIdInAndDeletedAtIsNull(new ArrayList<>(relatedPostIds));
            relatedPostsMap = relatedPosts.stream()
                    .collect(Collectors.toMap(Post::getId, p -> p));
        }

        // 4. 사용자의 좋아요 정보 일괄 조회
        List<Like> userLikes = likeRepository.findByUserIdAndPostIdInAndDeletedAtIsNull(user.getId(), postIds);
        Map<Long, Like> likesMap = userLikes.stream()
                .collect(Collectors.toMap(l -> l.getPost().getId(), l -> l));

        // 5. 사용자의 재게시 정보 일괄 조회
        List<Post> userReposts = postRepository.findByUserIdAndRepostIdInAndDeletedAtIsNull(user.getId(), postIds);
        Map<Long, Post> repostsMap = userReposts.stream()
                .collect(Collectors.toMap(Post::getRepostId, p -> p));

        // 6. 각 게시물에 대해 컨텍스트 정보 조합
        Map<Long, Post> finalRelatedPostsMap = relatedPostsMap;
        return posts.stream()
                .map(post -> {
                    Long viewCount = postViewService.getPostView(post.getId());

                    // 좋아요 정보
                    Like like = likesMap.get(post.getId());
                    boolean isLikedByMe = like != null;
                    Long likeIdByMe = like != null ? like.getId() : null;

                    // 재게시 정보
                    Post userRepost = repostsMap.get(post.getId());
                    boolean isRepostedByMe = userRepost != null;
                    Long repostIdByMe = userRepost != null ? userRepost.getId() : null;

                    // 관련 게시물
                    Post repostedPost = post.getRepostId() != null ? finalRelatedPostsMap.get(post.getRepostId()) : null;
                    Post quotedPost = post.getQuoteId() != null ? finalRelatedPostsMap.get(post.getQuoteId()) : null;
                    Post parentPost = post.getParentId() != null ? finalRelatedPostsMap.get(post.getParentId()) : null;

                    // 재게시 또는 인용한 사람 정보
                    Long repostedByUserId = null;
                    String repostedByUsername = null;
                    if (post.getRepostId() != null || post.getQuoteId() != null) {
                        repostedByUserId = post.getUser().getId();
                        repostedByUsername = post.getUser().getUsername();
                    }

                    return new PostWithUserContext(
                            post, viewCount, isLikedByMe, likeIdByMe, isRepostedByMe, repostIdByMe,
                            repostedPost, quotedPost, parentPost, repostedByUserId, repostedByUsername
                    );
                })
                .toList();
    }
}
