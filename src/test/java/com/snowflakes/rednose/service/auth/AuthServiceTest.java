package com.snowflakes.rednose.service.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.snowflakes.rednose.dto.auth.IssueTokenResult;
import com.snowflakes.rednose.entity.Member;
import com.snowflakes.rednose.exception.NotFoundException;
import com.snowflakes.rednose.exception.UnAuthorizedException;
import com.snowflakes.rednose.exception.errorcode.AuthErrorCode;
import com.snowflakes.rednose.exception.errorcode.MemberErrorCode;
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
        final String REFRESH_TOKEN_COOKIE =
                "refreshToken=" + REFRESH_TOKEN + "; Path=/; Secure; HttpOnly; SameSite=None";
        final String ACCESS_TOKEN_COOKIE =
                "accessToken=" + ACCESS_TOKEN + "; Path=/; Secure; HttpOnly; SameSite=None";
        IssueTokenResult expected = IssueTokenResult.builder()
                .refreshTokenCookie(REFRESH_TOKEN_COOKIE)
                .accessTokenCookie(ACCESS_TOKEN_COOKIE)
                .build();

        Member member = MemberFixture.builder().id(MEMBER_ID).build();

        when(memberRepository.findById(MEMBER_ID)).thenReturn(Optional.of(member));
        when(jwtTokenProvider.createAccessToken(MEMBER_ID)).thenReturn(REFRESH_TOKEN);
        when(jwtTokenProvider.createRefreshToken()).thenReturn(ACCESS_TOKEN);

        // when
        IssueTokenResult actual = authService.issueToken(MEMBER_ID);

        // then
        assertThat(actual.equals(expected));

    }

    @DisplayName("로그아웃을 성공적으로 할 수 있다")
    @Test
    public void 로그아웃_성공() {
        // given
        final Member 장지담 = MemberFixture.builder().socialId(1L).build();
        final String 토큰 = "ajkdlfajdlfkajl123klsdjlfaj";
        장지담.storeRefreshToken(토큰);
        assertThat(장지담.getRefreshToken()).isEqualTo(토큰);

        when(memberRepository.findById(장지담.getId())).thenReturn(Optional.of(장지담));

        // when
        authService.logout(장지담.getId());

        // then
        assertThat(장지담.getRefreshToken()).isEqualTo(null);
    }

    @DisplayName("로그아웃 과정에서 존재하지 않는 회원에 대해 알맞은 예외를 던진다")
    @Test
    public void 로그아웃_회원_존재하지_않음() {
        // given
        final Member 장지담 = MemberFixture.builder().socialId(1L).build();

        when(memberRepository.findById(장지담.getId())).thenReturn(Optional.empty());

        // when, then
        Assertions.assertThrows(NotFoundException.class, () -> authService.logout(장지담.getId()),
                MemberErrorCode.NOT_FOUND.getMessage());

    }
}