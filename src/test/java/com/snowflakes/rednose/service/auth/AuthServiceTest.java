package com.snowflakes.rednose.service.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.snowflakes.rednose.dto.auth.IssueTokenResult;
import com.snowflakes.rednose.entity.Member;
import com.snowflakes.rednose.exception.UnAuthorizedException;
import com.snowflakes.rednose.exception.errorcode.AuthErrorCode;
import com.snowflakes.rednose.repository.MemberRepository;
import com.snowflakes.rednose.support.fixture.MemberFixture;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private MemberRepository memberRepository;
    @InjectMocks
    private AuthService authService;

    @DisplayName("refreshToken으로 회원 찾기에 실패할 시 알맞은 예외를 던진다")
    @Test
    public void refreshToken으로_회원찾기_실패() {
        // given
        final String refreshToken = "flvmfptlxhzms123";
        when(jwtTokenProvider.verifySignature(refreshToken)).thenReturn(null);
        when(memberRepository.findByRefreshToken(refreshToken)).thenReturn(Optional.empty());

        // when, then
        Assertions.assertThrows(UnAuthorizedException.class, () -> authService.validateRefreshToken(refreshToken),
                AuthErrorCode.NOT_EXISTS_IN_DATABASE.getMessage());
    }

    @DisplayName("token 발급 후 알맞은 IssueTokenResult dto를 반환한다")
    @Test
    public void token발급_후_dto반환() {
        // given
        final Long MEMBER_ID = 1L;
        final String REFRESH_TOKEN = "flvmfptlxhzms123";
        final String ACCESS_TOKEN = "ajcptmxhzms123";
        final String COOKIE = "refreshToken=" + REFRESH_TOKEN + "; Path=/; Secure; HttpOnly; SameSite=None";
        IssueTokenResult expected = IssueTokenResult.builder().refreshTokenCookie(COOKIE).accessToken(ACCESS_TOKEN)
                .build();

        Member member = MemberFixture.builder().id(MEMBER_ID).build();

        when(jwtTokenProvider.createAccessToken(member)).thenReturn(REFRESH_TOKEN);
        when(jwtTokenProvider.createRefreshToken()).thenReturn(ACCESS_TOKEN);

        // when
        IssueTokenResult actual = authService.issueToken(member);

        // then
        assertThat(actual.equals(expected));

    }
}