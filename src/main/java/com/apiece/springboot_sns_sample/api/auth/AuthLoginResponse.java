package com.apiece.springboot_sns_sample.api.auth;

public record AuthLoginResponse(
        String sessionId,
        String username
) {
}
