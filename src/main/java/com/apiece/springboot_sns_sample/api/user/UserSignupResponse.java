package com.apiece.springboot_sns_sample.api.user;

import com.apiece.springboot_sns_sample.domain.user.User;

public record UserSignupResponse(
        Long id,
        String username
) {
    public static UserSignupResponse from(User user) {
        return new UserSignupResponse(user.getId(), user.getUsername());
    }
}
