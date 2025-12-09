package com.apiece.springboot_sns_sample.domain.post;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByDeletedAtIsNullOrderByCreatedAtDesc();

    Optional<Post> findByIdAndDeletedAtIsNull(Long id);

    List<Post> findAllByIdInAndDeletedAtIsNull(List<Long> ids);

    List<Post> findByParentIdAndDeletedAtIsNullOrderByCreatedAtAsc(Long parentId);

    List<Post> findByUserIdAndParentIdIsNullAndDeletedAtIsNullOrderByCreatedAtDesc(Long userId);

    List<Post> findByUserIdAndParentIdIsNotNullAndDeletedAtIsNullOrderByCreatedAtDesc(Long userId);

    boolean existsByUserIdAndQuoteIdAndDeletedAtIsNull(Long userId, Long quoteId);

    boolean existsByUserIdAndRepostIdAndDeletedAtIsNull(Long userId, Long repostId);

    Optional<Post> findByUserIdAndRepostIdAndDeletedAtIsNull(Long userId, Long repostId);
}
