package com.apiece.springboot_sns_sample.domain.media;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MediaRepository extends JpaRepository<Media, Long> {

    Optional<Media> findByIdAndDeletedAtIsNull(Long id);

    List<Media> findByUserIdAndDeletedAtIsNullOrderByCreatedAtDesc(Long userId);
}
