package com.snowflakes.rednose.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.snowflakes.rednose.dto.member.SignInRequest;
import com.snowflakes.rednose.exception.BadRequestException;
import com.snowflakes.rednose.exception.errorcode.MemberErrorCode;
import com.snowflakes.rednose.repository.MemberRepository;
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