package com.apiece.springboot_sns_sample.controller;

import com.apiece.springboot_sns_sample.domain.User;
import com.apiece.springboot_sns_sample.dto.UserSignupRequest;
import com.apiece.springboot_sns_sample.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public User signupUser(@RequestBody UserSignupRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        return userService.signupUser(user);
    }
}
