package com.apiece.springboot_sns_sample.api;

import com.apiece.springboot_sns_sample.api.user.UserResponse;
import com.apiece.springboot_sns_sample.api.user.UserSignupRequest;
import com.apiece.springboot_sns_sample.config.auth.AuthUser;
import com.apiece.springboot_sns_sample.domain.user.User;
import com.apiece.springboot_sns_sample.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/api/v1/users/signup")
    public UserResponse signupUser(@RequestBody UserSignupRequest request) {
        User user = userService.signup(request.username(), request.password());
        return UserResponse.from(user, null);
    }

    @GetMapping("/api/v1/users/me")
    public UserResponse getMyInfo(@AuthUser User user) {
        String profileImageUrl = userService.getProfileImageUrl(user);
        return UserResponse.from(user, profileImageUrl);
    }
}
