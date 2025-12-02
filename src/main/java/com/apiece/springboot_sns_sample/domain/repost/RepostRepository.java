package com.apiece.springboot_sns_sample.domain.repost;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RepostRepository extends JpaRepository<Repost, Long> {

    List<Repost> findAllByDeletedAtIsNullOrderByCreatedAtDesc();

    Optional<Repost> findByIdAndDeletedAtIsNull(Long id);

    List<Repost> findByUserIdAndDeletedAtIsNullOrderByCreatedAtDesc(Long userId);

    boolean existsByUserIdAndPostIdAndDeletedAtIsNull(Long userId, Long postId);
}
