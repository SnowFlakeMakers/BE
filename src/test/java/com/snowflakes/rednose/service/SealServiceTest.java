package com.snowflakes.rednose.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.snowflakes.rednose.dto.MakeSealResponse;
import com.snowflakes.rednose.dto.seal.AssignSealNameRequest;
import com.snowflakes.rednose.dto.seal.AssignSealNameResponse;
import com.snowflakes.rednose.dto.seal.MakeSealRequest;
import com.snowflakes.rednose.dto.seal.ShowSealSpecificResponse;
import com.snowflakes.rednose.entity.Member;
import com.snowflakes.rednose.entity.Seal;
import com.snowflakes.rednose.exception.NotFoundException;
import com.snowflakes.rednose.exception.UnAuthorizedException;
import com.snowflakes.rednose.exception.errorcode.AuthErrorCode;
import com.snowflakes.rednose.exception.errorcode.MemberErrorCode;
import com.snowflakes.rednose.exception.errorcode.SealErrorCode;
import com.snowflakes.rednose.repository.MemberRepository;
import com.snowflakes.rednose.repository.SealLikeRepository;
import com.snowflakes.rednose.repository.SealRepository;
import com.snowflakes.rednose.support.fixture.MemberFixture;
import com.snowflakes.rednose.support.fixture.SealFixture;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@Slf4j
class SealServiceTest {

    @Mock
    private SealRepository sealRepository;
    @Mock
    private SealLikeRepository sealLikeRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private SealService sealService;

    @DisplayName("알맞은 우표 자세히보기 응답을 얻을 수 있다")
    @Test
    void 우표_자세히보기() {
        // given
        final Long MEMBER_ID = 1L;
        final Member MEMBER = MemberFixture.builder().id(MEMBER_ID).build();

        final Long SEAL_ID = 1L;
        final int LIKES = 10;
        final Seal SEAL = SealFixture.builder().member(MEMBER).build();

        final boolean LIKED = true;

        when(sealRepository.findById(SEAL_ID)).thenReturn(Optional.of(SEAL));
        when(sealLikeRepository.existsByMemberIdAndSealId(MEMBER_ID, SEAL_ID)).thenReturn(LIKED);

        final ShowSealSpecificResponse EXPECTED = ShowSealSpecificResponse.of(SEAL, LIKED);

        // when
        final ShowSealSpecificResponse ACTUAL = sealService.showSpecific(SEAL_ID, MEMBER_ID);

        // then
        assertThat(ACTUAL).usingRecursiveComparison().isEqualTo(EXPECTED);
    }

    @DisplayName("씰을 만들 수 있다")
    @Test
    void 씰_만들기() {
        // given
        final Member JANG = MemberFixture.builder().id(1L).build();
        final Seal CHRISTMAS_SEAL = SealFixture.builder().id(2L).member(JANG).name(null).numberOfLikes(0).build();
        final MakeSealRequest REQUEST = MakeSealRequest.builder().image(CHRISTMAS_SEAL.getImageUrl()).build();

        when(memberRepository.findById(JANG.getId())).thenReturn(Optional.of(JANG));
        when(sealRepository.save(any(Seal.class))).thenReturn(CHRISTMAS_SEAL);

        final MakeSealResponse EXPECTED = MakeSealResponse.builder().image(CHRISTMAS_SEAL.getImageUrl())
                .sealId(CHRISTMAS_SEAL.getId()).build();

        // when
        final MakeSealResponse ACTUAL = sealService.make(JANG.getId(), REQUEST);

        assertAll(
                () -> verify(memberRepository, times(1)).findById(JANG.getId()),
                () -> verify(sealRepository, times(1)).save(any(Seal.class)),
                () -> assertThat(ACTUAL).usingRecursiveComparison().isEqualTo(EXPECTED)
        );
    }

    @DisplayName("씰을 만들 때 존재하지 않는 회원에 대해 예외를 던진다")
    @Test
    void 씰_만들기_회원없음() {
        // given
        final Member JANG = MemberFixture.builder().id(1L).build();
        final Seal CHRISTMAS_SEAL = SealFixture.builder().id(2L).member(JANG).name(null).numberOfLikes(0).build();
        final MakeSealRequest REQUEST = MakeSealRequest.builder().image(CHRISTMAS_SEAL.getImageUrl()).build();

        when(memberRepository.findById(JANG.getId())).thenReturn(Optional.empty());

        // when then
        assertThrows(NotFoundException.class, () -> sealService.make(JANG.getId(), REQUEST),
                MemberErrorCode.NOT_FOUND.getMessage());
    }

    @DisplayName("씰 이름을 지정할 수 있다")
    @Test
    void 씰_이름지정_성공() {
        // given
        final Member JANG = MemberFixture.builder().id(1L).build();
        final Seal CHRISTMAS_SEAL = SealFixture.builder().id(2L).member(JANG).name(null).numberOfLikes(0).build();
        final String NAME = "우리의 추억이 담긴 씰";
        final AssignSealNameRequest REQUEST = AssignSealNameRequest.builder().name(NAME)
                .sealId(CHRISTMAS_SEAL.getId()).build();

        when(memberRepository.findById(JANG.getId())).thenReturn(Optional.of(JANG));
        when(sealRepository.findById(CHRISTMAS_SEAL.getId())).thenReturn(Optional.of(CHRISTMAS_SEAL));

        final AssignSealNameResponse EXPECTED = AssignSealNameResponse.builder().sealId(CHRISTMAS_SEAL.getId())
                .image(CHRISTMAS_SEAL.getImageUrl()).name(NAME).build();

        // when
        final AssignSealNameResponse ACTUAL = sealService.assignName(JANG.getId(), REQUEST);

        // then
        assertAll(
                () -> verify(memberRepository, times(1)).findById(JANG.getId()),
                () -> verify(sealRepository, times(1)).findById(CHRISTMAS_SEAL.getId()),
                () -> assertThat(ACTUAL).usingRecursiveComparison().isEqualTo(EXPECTED)
        );
    }

    @DisplayName("씰 이름을 지정할 때 회원이 없으면 예외가 발생한다")
    @Test
    void 씰_이름지정_회원없음() {
        // given
        final Member JANG = MemberFixture.builder().id(1L).build();
        final Seal CHRISTMAS_SEAL = SealFixture.builder().id(2L).member(JANG).name(null).numberOfLikes(0).build();
        final String NAME = "우리의 추억이 담긴 씰";
        final AssignSealNameRequest REQUEST = AssignSealNameRequest.builder().name(NAME)
                .sealId(CHRISTMAS_SEAL.getId()).build();

        when(memberRepository.findById(JANG.getId())).thenReturn(Optional.empty());
        // when then
        assertThrows(NotFoundException.class, () -> sealService.assignName(JANG.getId(), REQUEST),
                MemberErrorCode.NOT_FOUND.getMessage());
    }

    @DisplayName("씰 이름을 지정할 때 씰이 없으면 예외가 발생한다")
    @Test
    void 씰_이름지정_씰없음() {
        // given
        final Member JANG = MemberFixture.builder().id(1L).build();
        final Seal CHRISTMAS_SEAL = SealFixture.builder().id(2L).member(JANG).name(null).numberOfLikes(0).build();
        final String NAME = "우리의 추억이 담긴 씰";
        final AssignSealNameRequest REQUEST = AssignSealNameRequest.builder().name(NAME)
                .sealId(CHRISTMAS_SEAL.getId()).build();

        when(memberRepository.findById(JANG.getId())).thenReturn(Optional.of(JANG));
        when(sealRepository.findById(CHRISTMAS_SEAL.getId())).thenReturn(Optional.empty());

        // when then
        assertThrows(NotFoundException.class, () -> sealService.assignName(JANG.getId(), REQUEST),
                SealErrorCode.NOT_FOUND.getMessage());
    }

    @DisplayName("씰 이름을 지정할 때 씰 제작자와 회원이 다르면 예외가 발생한다")
    @Test
    void 씰_이름지정_씰주인과다른회원() {
        // given
        final Member JANG = MemberFixture.builder().id(1L).build();
        final Member GIL = MemberFixture.builder().id(2L).build();
        final Seal CHRISTMAS_SEAL = SealFixture.builder().id(2L).member(JANG).name(null).numberOfLikes(0).build();
        final String NAME = "우리의 추억이 담긴 씰";
        final AssignSealNameRequest REQUEST = AssignSealNameRequest.builder().name(NAME)
                .sealId(CHRISTMAS_SEAL.getId()).build();

        when(memberRepository.findById(GIL.getId())).thenReturn(Optional.of(GIL));
        when(sealRepository.findById(CHRISTMAS_SEAL.getId())).thenReturn(Optional.of(CHRISTMAS_SEAL));

        // when, then
        assertThrows(UnAuthorizedException.class, () -> sealService.assignName(GIL.getId(), REQUEST),
                AuthErrorCode.NOT_SEAL_CREATOR.getMessage());
    }

}