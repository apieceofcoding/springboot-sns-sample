package com.apiece.springboot_sns_sample.domain.post;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByDeletedAtIsNullOrderByCreatedAtDesc();

    Optional<Post> findByIdAndDeletedAtIsNull(Long id);

    List<Post> findByParentIdAndDeletedAtIsNullOrderByCreatedAtAsc(Long parentId);

    List<Post> findByUserIdAndParentIsNullAndDeletedAtIsNullOrderByCreatedAtDesc(Long userId);

    List<Post> findByUserIdAndParentIsNotNullAndDeletedAtIsNullOrderByCreatedAtDesc(Long userId);
}
