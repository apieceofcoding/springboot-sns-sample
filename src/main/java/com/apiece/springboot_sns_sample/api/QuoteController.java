package com.apiece.springboot_sns_sample.api;

import com.apiece.springboot_sns_sample.api.quote.QuoteCreateRequest;
import com.apiece.springboot_sns_sample.api.quote.QuoteResponse;
import com.apiece.springboot_sns_sample.config.auth.AuthUser;
import com.apiece.springboot_sns_sample.domain.post.Post;
import com.apiece.springboot_sns_sample.domain.quote.QuoteService;
import com.apiece.springboot_sns_sample.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class QuoteController {

    private final QuoteService quoteService;

    @PostMapping("/api/v1/posts/{postId}/quotes")
    public QuoteResponse createQuote(
            @PathVariable Long postId,
            @RequestBody QuoteCreateRequest request,
            @AuthUser User user
    ) {
        Post quote = quoteService.createQuote(postId, request.content(), user);
        return QuoteResponse.from(quote);
    }
}
