package com.apiece.springboot_sns_sample.domain.follow;

import com.apiece.springboot_sns_sample.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    Optional<Follow> findByFollowerAndFolloweeAndDeletedAtIsNull(User follower, User followee);

    List<Follow> findByFollowerAndDeletedAtIsNull(User follower);

    List<Follow> findByFolloweeAndDeletedAtIsNull(User followee);

    boolean existsByFollowerAndFolloweeAndDeletedAtIsNull(User follower, User followee);

    long countByFollowerAndDeletedAtIsNull(User follower);

    long countByFolloweeAndDeletedAtIsNull(User followee);
}
