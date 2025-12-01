package com.apiece.springboot_sns_sample.domain.reply;

import com.apiece.springboot_sns_sample.domain.post.Post;
import com.apiece.springboot_sns_sample.domain.post.PostRepository;
import com.apiece.springboot_sns_sample.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final PostRepository postRepository;

    public Post createReply(Long parentId, String content, User user) {
        Post parent = postRepository.findByIdAndDeletedAtIsNull(parentId)
                .orElseThrow(() -> new IllegalArgumentException("Parent post not found: " + parentId));

        Post reply = Post.createReply(content, user, parent);
        return postRepository.save(reply);
    }

    public List<Post> getRepliesByParentId(Long parentId) {
        return postRepository.findByParentIdAndDeletedAtIsNullOrderByCreatedAtAsc(parentId);
    }

    @Transactional
    public Post updateReply(Long replyId, String content, User user) {
        Post reply = postRepository.findByIdAndDeletedAtIsNull(replyId)
                .orElseThrow(() -> new IllegalArgumentException("Reply not found: " + replyId));

        if (reply.getParent() == null) {
            throw new IllegalArgumentException("This is not a reply");
        }

        if (!reply.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You are not authorized to update this reply");
        }

        if (reply.isEditExpired()) {
            throw new IllegalArgumentException("Reply can only be edited within 1 hour of creation");
        }

        reply.updateContent(content);
        return reply;
    }

    public void deleteReply(Long replyId, User user) {
        Post reply = postRepository.findByIdAndDeletedAtIsNull(replyId)
                .orElseThrow(() -> new IllegalArgumentException("Reply not found: " + replyId));

        if (reply.getParent() == null) {
            throw new IllegalArgumentException("This is not a reply");
        }

        if (!reply.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You are not authorized to delete this reply");
        }

        reply.delete();
        postRepository.save(reply);
    }
}
