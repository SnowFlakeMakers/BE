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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;

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
        Slice<Seal> seals1 = sealRepository.findAllByMemberIdOrderByCreatedAtAsc(member1.getId(), pageRequest);
        Slice<Seal> seals2 = sealRepository.findAllByMemberIdOrderByCreatedAtAsc(member2.getId(), pageRequest);

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

    @Test
    void 씰_만든이의_이름으로_검색_가능하다() {
        final Member JANG = memberRepository.save(MemberFixture.builder().nickname("jang").build());
        final Member GIL = memberRepository.save(MemberFixture.builder().nickname("gil").build());

        final Seal seal1 = sealRepository.save(SealFixture.builder().member(JANG).build());
        final Seal seal2 = sealRepository.save(SealFixture.builder().member(GIL).build());
        final Seal seal3 = sealRepository.save(SealFixture.builder().member(JANG).build());

        PageRequest pageRequest1 = PageRequest.of(0, 2);

        Page<Seal> seals1 = sealRepository.findAllAtBoard(JANG.getNickname(), pageRequest1);
        Page<Seal> seals2 = sealRepository.findAllAtBoard(GIL.getNickname(), pageRequest1);

        assertAll(
                () -> assertThat(seals1.getContent()).contains(seal1, seal3),
                () -> assertThat(seals2.getContent()).contains(seal2)
        );
    }

    @Test
    void 씰_이름으로_검색_가능하다() {
        final Member member1 = memberRepository.save(MemberFixture.builder().build());
        final Member member2 = memberRepository.save(MemberFixture.builder().build());

        final Seal seal1 = sealRepository.save(SealFixture.builder().member(member1).name("seal1").build());
        final Seal seal2 = sealRepository.save(SealFixture.builder().member(member2).name("seal2").build());
        final Seal seal3 = sealRepository.save(SealFixture.builder().member(member1).name("seal3").build());

        PageRequest pageRequest1 = PageRequest.of(0, 1);
        PageRequest pageRequest2 = PageRequest.of(0, 3);

        Page<Seal> seals1 = sealRepository.findAllAtBoard("2", pageRequest1);
        Page<Seal> seals2 = sealRepository.findAllAtBoard("seal3", pageRequest1);
        Page<Seal> seals3 = sealRepository.findAllAtBoard("seal", pageRequest2);

        assertAll(
                () -> assertThat(seals1.getContent()).contains(seal2),
                () -> assertThat(seals2.getContent()).contains(seal3),
                () -> assertThat(seals3.getContent()).contains(seal1, seal2, seal3)
        );
    }

    @Test
    void 좋아요순으로_조회_가능하다() {
        final Member member1 = memberRepository.save(MemberFixture.builder().build());
        final Member member2 = memberRepository.save(MemberFixture.builder().build());

        final Seal seal1 = sealRepository.save(SealFixture.builder().member(member1).numberOfLikes(2).build());
        final Seal seal2 = sealRepository.save(SealFixture.builder().member(member2).numberOfLikes(3).build());
        final Seal seal3 = sealRepository.save(SealFixture.builder().member(member1).numberOfLikes(5).build());
        final Seal seal4 = sealRepository.save(SealFixture.builder().member(member2).numberOfLikes(1).build());

        PageRequest pageRequest1 = PageRequest.of(0, 4, Sort.by(Sort.Direction.DESC, "numberOfLikes"));

        Page<Seal> seals1 = sealRepository.findAllAtBoard(null, pageRequest1);
        assertThat(seals1.getContent()).containsExactly(seal3, seal2, seal1, seal4);
    }
}