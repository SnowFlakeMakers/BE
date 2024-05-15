package com.snowflakes.rednose.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.snowflakes.rednose.dto.auth.UserInfo;
import com.snowflakes.rednose.dto.member.SignInRequest;
import com.snowflakes.rednose.exception.BadRequestException;
import com.snowflakes.rednose.exception.NotFoundException;
import com.snowflakes.rednose.exception.errorcode.MemberErrorCode;
import com.snowflakes.rednose.repository.MemberRepository;
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
        final String NICKNAME = "마치된거같아손오공";
        final Long SOCIAL_ID = 123213L;
        when(memberRepository.existsByNickname(NICKNAME)).thenReturn(false);

        // when
        memberService.signIn(SignInRequest.builder().nickname(NICKNAME).socialId(SOCIAL_ID).build());

        // then
        verify(memberRepository, times(1)).save(any());
    }

    @DisplayName("닉네임 중복에 걸린다면 알맞은 예외를 던진다")
    @Test
    void 회원가입_닉네임중복() {
        // given
        final String DUPLICATED_NICKNAME = "HEY반짝이는YOURMYSTYLE";
        final Long SOCIAL_ID = 123213L;
        when(memberRepository.existsByNickname(DUPLICATED_NICKNAME)).thenReturn(true);

        // when, then
        assertThrows(BadRequestException.class, () -> memberService.signIn(
                        SignInRequest.builder().nickname(DUPLICATED_NICKNAME).socialId(SOCIAL_ID).build()),
                MemberErrorCode.DUPLICATED_NICKNAME.getMessage());
    }

    @DisplayName("socialId로 회원을 찾지 못하면 알맞은 예외를 던진다")
    @Test
    void 소셜아이디_회원없음() {
        // given
        final Long SOCIAL_ID = 913091309130913L;
        when(memberRepository.findBySocialId(SOCIAL_ID)).thenReturn(Optional.empty());

        // when, then
        assertThrows(NotFoundException.class,
                () -> memberService.getExistMember(UserInfo.builder().id(SOCIAL_ID).build()),
                MemberErrorCode.NOT_FOUND.getMessage());
    }
}