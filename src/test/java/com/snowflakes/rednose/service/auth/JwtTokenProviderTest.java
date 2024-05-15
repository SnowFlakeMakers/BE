package com.snowflakes.rednose.service.auth;

import static org.assertj.core.api.Assertions.assertThat;

import com.snowflakes.rednose.entity.Member;
import com.snowflakes.rednose.exception.UnAuthorizedException;
import com.snowflakes.rednose.exception.errorcode.AuthErrorCode;
import com.snowflakes.rednose.support.fixture.MemberFixture;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
class JwtTokenProviderTest {

    private final String ID = "id";
    private final String NICKNAME = "nickname";
    private final String ISSUER = "rednose";
    private final String SECRET_KEY = "tlqkfdhodksehlsmsrjdi";
    private final String ENCODED_SECRET_KEY = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
    private final String ENCODED_DIFFERENTLY_SECRET_KEY = new String(SECRET_KEY.getBytes(), StandardCharsets.US_ASCII);
    private final String FAKE_KEY = "dmddodmddodmddodmddodmddodmddodmddo";
    private final String ENCODED_FAKE_KEY = Base64.getEncoder().encodeToString(FAKE_KEY.getBytes());
    private final Long ACCESS_TOKEN_VALID_TIME = 1800000L;
    private final Long SHORT_TIME = 10000L;

    private final JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(SECRET_KEY);

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
        String accessToken = createAccessToken(member, SHORT_TIME, ENCODED_SECRET_KEY);
        Assertions.assertDoesNotThrow(() -> jwtTokenProvider.verifySignature(accessToken));

        TimeUnit.MILLISECONDS.sleep(SHORT_TIME + 10L);

        // then
        Assertions.assertThrows(UnAuthorizedException.class, () -> jwtTokenProvider.verifySignature(accessToken),
                AuthErrorCode.EXPIRED.getMessage());
    }

    @DisplayName("지원하지 않는 토큰에 대해 알맞은 예외를 던진다")
    @Test
    void token_미지원() {
        // given
        final Long MEMBER_ID = 1L;
        Member member = MemberFixture.builder().id(MEMBER_ID).build();

        // when
        String accessToken = createAccessToken(member, ACCESS_TOKEN_VALID_TIME, ENCODED_FAKE_KEY);

        // then
        Assertions.assertThrows(UnAuthorizedException.class, () -> jwtTokenProvider.verifySignature(accessToken),
                AuthErrorCode.UNSUPPORTED.getMessage());
    }

    @DisplayName("잘못된 구조의 토큰에 대해 알맞은 예외를 던진다")
    @Test
    void token_구조이상() {
        // given
        final Long MEMBER_ID = 1L;
        Member member = MemberFixture.builder().id(MEMBER_ID).build();

        // when
        String accessToken = jwtTokenProvider.createAccessToken(member);
        String malformedToken = accessToken + "!@#@#MalFoRm!@#!@#";

        // then
        Assertions.assertThrows(UnAuthorizedException.class, () -> jwtTokenProvider.verifySignature(malformedToken),
                AuthErrorCode.MALFORMED.getMessage());
    }

    @DisplayName("토큰 서명 실패에 대해 알맞은 예외를 던진다")
    @Test
    void token_서명실패() {
        // given
        final Long MEMBER_ID = 1L;
        Member member = MemberFixture.builder().id(MEMBER_ID).build();

        // when
        String failSignatureToken = createAccessToken(member, ACCESS_TOKEN_VALID_TIME,
                ENCODED_DIFFERENTLY_SECRET_KEY);

        // then
        Assertions.assertThrows(UnAuthorizedException.class, () -> jwtTokenProvider.verifySignature(failSignatureToken),
                AuthErrorCode.SIGNATURE.getMessage());
    }

    public String createAccessToken(Member member, Long accessTokenValidTime, String encodedKey) {
        // claim : id, nickname
        Map<String, Object> claims = new HashMap<>();
        claims.put(ID, member.getId());
        claims.put(NICKNAME, member.getNickname());

        // 발행시간
        Date now = new Date();

        // access 토큰 발행
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenValidTime))
                .setIssuer(ISSUER)
                .signWith(SignatureAlgorithm.HS512, encodedKey)
                .compact();
    }
}
