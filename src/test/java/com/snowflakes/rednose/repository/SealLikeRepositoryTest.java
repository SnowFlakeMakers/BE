package com.snowflakes.rednose.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.snowflakes.rednose.entity.Member;
import com.snowflakes.rednose.entity.Seal;
import com.snowflakes.rednose.entity.SealLike;
import com.snowflakes.rednose.support.RepositoryTest;
import com.snowflakes.rednose.support.fixture.MemberFixture;
import com.snowflakes.rednose.support.fixture.SealFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class SealLikeRepositoryTest {

    @Autowired
    private SealLikeRepository sealLikeRepository;

    @Autowired
    private SealRepository sealRepository;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("memberId와 sealId로 SealLike의 존재여부를 확인할 수 있다")
    @Test
    void memberId_sealId로_존재여부_확인() {
        // given
        final Member JANG = 저장(MemberFixture.builder().build());

        final Seal CHRISTMAS_SEAL = 저장(SealFixture.builder().member(JANG).build());
        final Seal BIRTHDAY_SEAL = 저장(SealFixture.builder().member(JANG).build());

        sealLikeRepository.save(SealLike.builder().member(JANG).seal(CHRISTMAS_SEAL).build());

        // when
        final boolean JANG_CHRISTMAS = sealLikeRepository.existsByMemberIdAndSealId(JANG.getId(),
                CHRISTMAS_SEAL.getId());
        final boolean JANG_BIRTHDAY = sealLikeRepository.existsByMemberIdAndSealId(JANG.getId(), BIRTHDAY_SEAL.getId());

        // then
        assertAll(
                () -> assertThat(JANG_CHRISTMAS).isEqualTo(true),
                () -> assertThat(JANG_BIRTHDAY).isEqualTo(false)
        );
    }

    private Seal 저장(Seal CHRISTMAS_SEAL) {
        return sealRepository.save(CHRISTMAS_SEAL);
    }

    private Member 저장(Member JANG) {
        return memberRepository.save(JANG);
    }

}