package com.snowflakes.rednose.repository;

import com.snowflakes.rednose.entity.StampLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StampLikeRepository extends JpaRepository<StampLike, Long> {

    boolean existsByMemberIdAndStampId(Long memberId, Long stampId);


}
