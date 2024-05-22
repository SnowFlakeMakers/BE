package com.snowflakes.rednose.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.snowflakes.rednose.entity.Member;
import com.snowflakes.rednose.entity.Stamp;
import com.snowflakes.rednose.entity.StampLike;
import com.snowflakes.rednose.repository.MemberRepository;
import com.snowflakes.rednose.repository.StampLikeRepository;
import com.snowflakes.rednose.repository.stamp.StampRepository;
import com.snowflakes.rednose.support.fixture.MemberFixture;
import com.snowflakes.rednose.support.fixture.StampFixture;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@Slf4j
class StampLikeServiceTest {

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private StampRepository stampRepository;
    @Mock
    private StampLikeRepository stampLikeRepository;

    @InjectMocks
    private StampLikeService stampLikeService;

    @Test
    void 좋아요를_누를_수_있다() {
        Member member = MemberFixture.builder().id(1L).build();
        Stamp stamp = StampFixture.builder().id(1L).build();

        given(stampRepository.findById(1L)).willReturn(Optional.of(stamp));
        given(memberRepository.findById(1L)).willReturn(Optional.of(member));
        given(stampLikeRepository.existsByMemberIdAndStampId(1L, 1L)).willReturn(false);

        assertAll(
                () -> stampLikeService.like(stamp.getId(), member.getId()),
                () -> verify(memberRepository, times(1)).findById(1L),
                () -> verify(stampRepository, times(1)).findById(1L),
                () -> verify(stampLikeRepository, times(1)).save(any(StampLike.class))
        );
    }

    @Test
    void 좋아요_취소가_가능하다() {
        Member member = MemberFixture.builder().id(1L).build();
        Stamp stamp = StampFixture.builder().id(1L).build();
        StampLike stampLike = StampLike.builder().stamp(stamp).member(member).build();
        given(memberRepository.existsById(member.getId())).willReturn(true);
        given(stampRepository.findById(stamp.getId())).willReturn(Optional.of(stamp));
        given(stampLikeRepository.findByMemberIdAndStampId(member.getId(), stamp.getId())).willReturn(
                Optional.of(stampLike));

        assertAll(
                () -> stampLikeService.cancel(member.getId(), stamp.getId()),
                () -> verify(memberRepository, times(1)).existsById(member.getId()),
                () -> verify(stampRepository, times(1)).findById(stamp.getId()),
                () -> verify(stampLikeRepository, times(1)).findByMemberIdAndStampId(member.getId(), stamp.getId()),
                () -> verify(stampLikeRepository, times(1)).delete(stampLike)
        );
    }
}