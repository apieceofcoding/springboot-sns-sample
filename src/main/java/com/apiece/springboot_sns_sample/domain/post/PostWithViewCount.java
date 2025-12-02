package com.apiece.springboot_sns_sample.domain.post;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PostWithViewCount {
    private final Post post;
    private final Long viewCount;
}
