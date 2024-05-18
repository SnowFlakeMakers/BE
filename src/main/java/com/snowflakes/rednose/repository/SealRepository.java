package com.snowflakes.rednose.repository;

import com.snowflakes.rednose.entity.Seal;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SealRepository extends JpaRepository<Seal, Long> {

    Slice<Seal> findAllByMemberId(Long memberId, Pageable pageable);

    @Query("select s from Seal s join SealLike sl on s.id = sl.seal.id where sl.member.id = :memberId")
    Slice<Seal> findMyLikesByMemberId(@Param("memberId") Long memberId, Pageable pageable);

}
