package com.apiece.springboot_sns_sample.domain.quote;

import com.apiece.springboot_sns_sample.domain.post.Post;
import com.apiece.springboot_sns_sample.domain.post.PostRepository;
import com.apiece.springboot_sns_sample.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuoteService {

    private final PostRepository postRepository;

    public Post createQuote(Long quoteId, String content, User user) {
        Post quotedPost = postRepository.findByIdAndDeletedAtIsNull(quoteId)
                .orElseThrow(() -> new IllegalArgumentException("Quoted post not found: " + quoteId));

        Post quote = Post.createQuote(content, user, quotedPost);
        return postRepository.save(quote);
    }
}
