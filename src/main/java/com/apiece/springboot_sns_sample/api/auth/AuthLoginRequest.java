package com.apiece.springboot_sns_sample.api.auth;

public record AuthLoginRequest(
        String username,
        String password
) {
}
