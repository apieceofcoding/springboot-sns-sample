package com.apiece.springboot_sns_sample.repository;

import com.apiece.springboot_sns_sample.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
