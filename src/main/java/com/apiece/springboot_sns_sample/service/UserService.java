package com.apiece.springboot_sns_sample.service;

import com.apiece.springboot_sns_sample.domain.User;
import com.apiece.springboot_sns_sample.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User signupUser(User user) {
        return userRepository.save(user);
    }
}
