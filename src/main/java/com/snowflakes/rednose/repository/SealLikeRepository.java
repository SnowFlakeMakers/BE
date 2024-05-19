package com.snowflakes.rednose.repository;

import com.snowflakes.rednose.entity.SealLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SealLikeRepository extends JpaRepository<SealLike, Long> {
    boolean existsByMemberIdAndSealId(Long memberId, Long sealId);
}
