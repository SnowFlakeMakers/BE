package com.snowflakes.rednose.repository;

import com.snowflakes.rednose.entity.Seal;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SealRepository extends JpaRepository<Seal, Long> {

    Slice<Seal> findAllByMemberId(Long memberId, Pageable pageable);

}
