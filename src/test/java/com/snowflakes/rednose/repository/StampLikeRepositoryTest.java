package com.snowflakes.rednose.repository;

import com.snowflakes.rednose.entity.Member;
import com.snowflakes.rednose.entity.Stamp;
import com.snowflakes.rednose.entity.StampLike;
import com.snowflakes.rednose.support.fixture.MemberFixture;
import com.snowflakes.rednose.fixture.StampFixture;
import com.snowflakes.rednose.repository.stamp.StampRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class StampLikeRepositoryTest {

    @Autowired private StampLikeRepository stampLikeRepository;
    @Autowired private StampRepository stampRepository;
    @Autowired private MemberRepository memberRepository;

    @Test
    void memberId와_stampId로_좋아요_기록을_찾을_수_있다() {
        Stamp stamp1 = stampRepository.save(StampFixture.builder().id(1L).build());
        Stamp stamp2 = stampRepository.save(StampFixture.builder().id(2L).build());
        Member member = memberRepository.save(MemberFixture.builder().id(1L).build());
        stampLikeRepository.save(StampLike.builder().member(member).stamp(stamp1).build());

        assertAll(
                () -> assertThat(stampLikeRepository.existsByMemberIdAndStampId(member.getId(), stamp1.getId())).isTrue(),
                () -> assertThat(stampLikeRepository.existsByMemberIdAndStampId(member.getId(), stamp2.getId())).isFalse()
        );

    }
}