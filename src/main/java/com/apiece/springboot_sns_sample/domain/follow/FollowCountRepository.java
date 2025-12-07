package com.apiece.springboot_sns_sample.domain.follow;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowCountRepository extends JpaRepository<FollowCount, Long> {

    Optional<FollowCount> findByUserIdAndDeletedAtIsNull(Long userId);
}
