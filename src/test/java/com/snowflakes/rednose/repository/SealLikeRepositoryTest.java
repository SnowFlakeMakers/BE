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
        final Member JANG = MemberFixture.builder().build();

        final Long JANG_ID = memberRepository.save(JANG).getId();

        final Seal CHRISTMAS_SEAL = SealFixture.builder().member(JANG).build();
        final Seal BIRTHDAY_SEAL = SealFixture.builder().member(JANG).build();

        final Long CHRISTMAS_SEAL_ID = sealRepository.save(CHRISTMAS_SEAL).getId();
        final Long BIRTHDAY_SEAL_ID = sealRepository.save(BIRTHDAY_SEAL).getId();

        sealLikeRepository.save(SealLike.builder().member(JANG).seal(CHRISTMAS_SEAL).build());

        // when
        final boolean JANG_CHRISTMAS = sealLikeRepository.existsByMemberIdAndSealId(JANG_ID, CHRISTMAS_SEAL_ID);
        final boolean JANG_BIRTHDAY = sealLikeRepository.existsByMemberIdAndSealId(JANG_ID, BIRTHDAY_SEAL_ID);

        // then
        assertAll(
                () -> assertThat(JANG_CHRISTMAS).isEqualTo(true),
                () -> assertThat(JANG_BIRTHDAY).isEqualTo(false)
        );
    }

}