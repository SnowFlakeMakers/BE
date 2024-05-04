package com.snowflakes.rednose.repository;

import com.snowflakes.rednose.entity.StampLike;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StampLikeRepository extends JpaRepository<StampLike, Long> {

    boolean existsByMemberIdAndStampId(Long memberId, Long stampId);

    Optional<StampLike> findByMemberIdAndStampId(Long memberId, Long stampId);


}
