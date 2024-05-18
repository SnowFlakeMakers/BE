package com.snowflakes.rednose.service;

import com.snowflakes.rednose.entity.Member;
import com.snowflakes.rednose.entity.Seal;
import com.snowflakes.rednose.entity.SealLike;
import com.snowflakes.rednose.exception.BadRequestException;
import com.snowflakes.rednose.repository.MemberRepository;
import com.snowflakes.rednose.repository.SealLikeRepository;
import com.snowflakes.rednose.repository.SealRepository;
import com.snowflakes.rednose.support.fixture.MemberFixture;
import com.snowflakes.rednose.support.fixture.SealFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SealLikeServiceTest {

    @Mock private MemberRepository memberRepository;
    @Mock private SealRepository sealRepository;
    @Mock private SealLikeRepository sealLikeRepository;
    @InjectMocks
    private SealLikeService sealLikeService;

    @Test
    void 좋아요를_누를_수_있다() {
        Member member = MemberFixture.builder().build();
        Seal seal = SealFixture.builder().build();
        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));
        given(sealRepository.findById(seal.getId())).willReturn(Optional.of(seal));
        given(sealLikeRepository.existsByMemberIdAndSealId(member.getId(), seal.getId())).willReturn(false);

        assertAll(
                () -> sealLikeService.like(seal.getId(), member.getId()),
                () -> verify(memberRepository, times(1)).findById(member.getId()),
                () -> verify(sealRepository, times(1)).findById(seal.getId()),
                () -> verify(sealLikeRepository, times(1)).save(any(SealLike.class))
        );
    }

    @Test
    void 이미_좋아요를_누른_경우_예외() {
        Member member = MemberFixture.builder().build();
        Seal seal = SealFixture.builder().build();
        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));
        given(sealRepository.findById(seal.getId())).willReturn(Optional.of(seal));
        given(sealLikeRepository.existsByMemberIdAndSealId(member.getId(), seal.getId())).willReturn(true);

        assertAll(
                () -> assertThatThrownBy(() -> sealLikeService.like(seal.getId(), member.getId()))
                        .isExactlyInstanceOf(BadRequestException.class),
                () -> verify(memberRepository, times(1)).findById(member.getId()),
                () -> verify(sealRepository, times(1)).findById(seal.getId()),
                () -> verify(sealLikeRepository, times(0)).save(any(SealLike.class))
        );
    }

}