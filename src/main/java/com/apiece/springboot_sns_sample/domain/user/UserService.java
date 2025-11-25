package com.apiece.springboot_sns_sample.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User signupUser(String username, String password) {
        User user = User.create(username, password);
        return userRepository.save(user);
    }
}
