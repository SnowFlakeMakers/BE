package com.snowflakes.rednose.repository;

import com.snowflakes.rednose.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findById(Long memberId);

    boolean existsById(Long memberId);


    Optional<Member> findBySocialId(Long socialId);

    boolean existsByNickname(String nickname);
}
