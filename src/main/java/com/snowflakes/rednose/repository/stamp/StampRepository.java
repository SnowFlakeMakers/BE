package com.snowflakes.rednose.repository.stamp;

import com.snowflakes.rednose.entity.Stamp;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StampRepository extends JpaRepository<Stamp, Long> {
    Optional<Stamp> findById(Long stampId);

    @Query("select s from Stamp s join StampLike sl on s.id = sl.stamp.id where sl.member.id = :memberId")
    Slice<Stamp> findLikesByMemberId(@Param("memberId") Long memberId, Pageable pageable);

    @Query("select s from Stamp s join StampRecord sr on s.id = sr.stamp.id where sr.member.id = :memberId")
    Slice<Stamp> findMyStampsByMemberId(@Param("memberId") Long memberId, Pageable pageable);
}
