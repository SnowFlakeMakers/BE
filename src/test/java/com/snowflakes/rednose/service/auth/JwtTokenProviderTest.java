package com.snowflakes.rednose.service.auth;

import static org.assertj.core.api.Assertions.assertThat;

import com.snowflakes.rednose.entity.Member;
import com.snowflakes.rednose.support.fixture.MemberFixture;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
class JwtTokenProviderTest {


    private final String secretKey = "tlqkfdhodksehlsmsrjdi";

    private final JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(secretKey);

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


}