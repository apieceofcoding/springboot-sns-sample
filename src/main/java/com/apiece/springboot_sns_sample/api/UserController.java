package com.apiece.springboot_sns_sample.api;

import com.apiece.springboot_sns_sample.api.user.UserSignupRequest;
import com.apiece.springboot_sns_sample.api.user.UserSignupResponse;
import com.apiece.springboot_sns_sample.domain.user.User;
import com.apiece.springboot_sns_sample.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/api/v1/users/signup")
    public UserSignupResponse signupUser(@RequestBody UserSignupRequest request) {
        User user = userService.signup(request.username(), request.password());
        return UserSignupResponse.from(user);
    }
}
