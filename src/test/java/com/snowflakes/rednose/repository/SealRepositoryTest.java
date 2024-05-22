package com.snowflakes.rednose.repository;

import com.snowflakes.rednose.entity.Member;
import com.snowflakes.rednose.entity.Seal;
import com.snowflakes.rednose.entity.SealLike;
import com.snowflakes.rednose.support.RepositoryTest;
import com.snowflakes.rednose.support.fixture.MemberFixture;
import com.snowflakes.rednose.support.fixture.SealFixture;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Slf4j
@RepositoryTest
class SealRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private SealRepository sealRepository;
    @Autowired
    private SealLikeRepository sealLikeRepository;

    @Test
    void memberId로_내가_만든_씰을_조회할_수_있다() {
        Member member1 = memberRepository.save(MemberFixture.builder().build());
        Member member2 = memberRepository.save(MemberFixture.builder().build());

        Seal seal1 = sealRepository.save(SealFixture.builder().member(member1).build());
        Seal seal2 = sealRepository.save(SealFixture.builder().member(member1).build());
        Seal seal3 = sealRepository.save(SealFixture.builder().member(member2).build());

        PageRequest pageRequest = PageRequest.of(0, 2);
        Slice<Seal> seals1 = sealRepository.findAllByMemberId(member1.getId(), pageRequest);
        Slice<Seal> seals2 = sealRepository.findAllByMemberId(member2.getId(), pageRequest);

        assertAll(
                () -> assertThat(seals1.getContent()).containsExactly(seal1, seal2),
                () -> assertThat(seals2.getContent()).containsExactly(seal3)
        );
    }

    @Test
    void 내가_좋아요한_씰들을_조회할_수_있다() {

        Member member1 = memberRepository.save(MemberFixture.builder().build());
        Member member2 = memberRepository.save(MemberFixture.builder().build());

        Seal seal1 = sealRepository.save(SealFixture.builder().member(member2).build());
        Seal seal2 = sealRepository.save(SealFixture.builder().member(member2).build());
        Seal seal3 = sealRepository.save(SealFixture.builder().member(member1).build());

        sealLikeRepository.save(SealLike.builder().seal(seal1).member(member1).build());
        sealLikeRepository.save(SealLike.builder().seal(seal2).member(member1).build());
        sealLikeRepository.save(SealLike.builder().seal(seal3).member(member2).build());

        PageRequest pageRequest1 = PageRequest.of(0, 1);
        PageRequest pageRequest2 = PageRequest.of(1, 1);

        Slice<Seal> seals1 = sealRepository.findMyLikesByMemberId(member1.getId(), pageRequest1);
        Slice<Seal> seals2 = sealRepository.findMyLikesByMemberId(member1.getId(), pageRequest2);
        Slice<Seal> seals3 = sealRepository.findMyLikesByMemberId(member2.getId(), pageRequest1);
        Slice<Seal> seals4 = sealRepository.findMyLikesByMemberId(member2.getId(), pageRequest2);

        assertAll(
                () -> assertThat(seals1.getContent()).containsExactly(seal1),
                () -> assertThat(seals2.getContent()).containsExactly(seal2),
                () -> assertThat(seals3.getContent()).containsExactly(seal3),
                () -> assertThat(seals4.getContent()).isEmpty()
        );
    }
}