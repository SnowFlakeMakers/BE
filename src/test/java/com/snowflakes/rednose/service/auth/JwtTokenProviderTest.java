package com.snowflakes.rednose.service.auth;

import static org.assertj.core.api.Assertions.assertThat;

import com.snowflakes.rednose.entity.Member;
import com.snowflakes.rednose.exception.UnAuthorizedException;
import com.snowflakes.rednose.support.fixture.MemberFixture;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
class JwtTokenProviderTest {
    private final String FAKE_SECRET_KEY = "tlqkfdhodksehlsmsrjdi";
    private final Long ACCESS_TOKEN_VALID_TIME = 1800000L;
    private final Long REFRESH_TOKEN_VALID_TIME = 1800000L;

    private final Long SHORT_TIME = 1000L;

    private final JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(FAKE_SECRET_KEY, ACCESS_TOKEN_VALID_TIME,
            REFRESH_TOKEN_VALID_TIME);

    private final JwtTokenProvider jwtShortTokenProvider = new JwtTokenProvider(FAKE_SECRET_KEY, SHORT_TIME,
            REFRESH_TOKEN_VALID_TIME);

    @DisplayName("access token을 발행하고 검증할 수 있다")
    @Test
    void accessToken_발행_검증() {
        // given
        final Long MEMBER_ID = 1L;
        Member member = MemberFixture.builder().id(MEMBER_ID).build();

        // when
        String accessToken = jwtTokenProvider.createAccessToken(member);
        log.info("access token generated : {}", accessToken);
        Long memberId = jwtTokenProvider.getMemberId(accessToken);

        // then
        assertThat(memberId).isEqualTo(MEMBER_ID);
    }

    @DisplayName("refresh token을 발행하고 검증할 수 있다")
    @Test
    void refreshToken_발행_검증() {
        // when
        String refreshToken = jwtTokenProvider.createRefreshToken();
        log.info("refresh token generated : {}", refreshToken);

        // then (검증 과정에서 예외를 던지지 않는 것을 테스트)
        Assertions.assertDoesNotThrow(() -> jwtTokenProvider.verifySignature(refreshToken));
    }

    @DisplayName("만료된 토큰에 대해 알맞은 예외를 던진다")
    @Test
    void token_만료() throws InterruptedException {
        // given
        final Long MEMBER_ID = 1L;
        Member member = MemberFixture.builder().id(MEMBER_ID).build();

        // when
        String accessToken = jwtShortTokenProvider.createAccessToken(member);
        Assertions.assertDoesNotThrow(() -> jwtShortTokenProvider.verifySignature(accessToken));

        TimeUnit.MILLISECONDS.sleep(10000);

        // then
        Assertions.assertThrows(UnAuthorizedException.class, () -> jwtTokenProvider.verifySignature(accessToken));
    }


}