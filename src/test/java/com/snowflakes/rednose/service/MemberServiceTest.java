package com.snowflakes.rednose.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.snowflakes.rednose.dto.member.SignInRequest;
import com.snowflakes.rednose.entity.Member;
import com.snowflakes.rednose.exception.BadRequestException;
import com.snowflakes.rednose.exception.errorcode.MemberErrorCode;
import com.snowflakes.rednose.repository.MemberRepository;
import com.snowflakes.rednose.support.fixture.MemberFixture;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;
    @InjectMocks
    private MemberService memberService;

    @DisplayName("닉네임 중복에 걸리지 않는다면 회원가입에 성공한다")
    @Test
    void 회원가입() {
        // given
        final Long MEMBER_ID = 1L;
        final String NICKNAME = "마치된거같아손오공";
        final Member MEMBER = MemberFixture.builder().id(MEMBER_ID).build();
        when(memberRepository.existsByNickname(NICKNAME)).thenReturn(false);
        when(memberRepository.findById(MEMBER_ID)).thenReturn(Optional.of(MEMBER));

        // when
        final Member SIGNED_IN_MEMBER = memberService.signIn(SignInRequest.builder().nickname(NICKNAME).build(),
                MEMBER_ID);

        // then
        assertThat(SIGNED_IN_MEMBER.getNickname()).isEqualTo(NICKNAME);
    }

    @DisplayName("닉네임 중복에 걸린다면 알맞은 예외를 던진다")
    @Test
    void 회원가입_닉네임중복() {
        // given
        final String DUPLICATED_NICKNAME = "HEY반짝이는YOURMYSTYLE";
        final Long MEMBER_ID = 1L;

        when(memberRepository.existsByNickname(DUPLICATED_NICKNAME)).thenReturn(true);

        // when, then
        assertThrows(BadRequestException.class, () -> memberService.signIn(
                        SignInRequest.builder().nickname(DUPLICATED_NICKNAME).build(), MEMBER_ID),
                MemberErrorCode.DUPLICATED_NICKNAME.getMessage());
    }
}