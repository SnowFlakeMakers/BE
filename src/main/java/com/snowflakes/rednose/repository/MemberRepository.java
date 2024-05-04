package com.snowflakes.rednose.repository;

import com.snowflakes.rednose.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findById(Long memberId);

    boolean existsById(Long memberId);
}
