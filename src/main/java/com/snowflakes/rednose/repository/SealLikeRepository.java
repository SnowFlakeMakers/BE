package com.snowflakes.rednose.repository;

import com.snowflakes.rednose.entity.SealLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SealLikeRepository extends JpaRepository<SealLike, Long> {

    boolean existsByMemberIdAndSealId(Long memberId, Long sealId);
}
