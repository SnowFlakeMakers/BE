package com.snowflakes.rednose.service.auth;

import com.snowflakes.rednose.entity.Member;
import com.snowflakes.rednose.exception.UnAuthorizedException;
import com.snowflakes.rednose.exception.errorcode.AuthErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    private String encodedKey;

    private final long accessTokenValidTime = 30 * 60 * 1000L;
    private final long MILLISECONDS_PER_WEEK = 7 * 24 * 60 * 60 * 1000L;
    private final long refreshTokenValidTime = 2 * MILLISECONDS_PER_WEEK;

    // jwt key 암호화
    protected void init(@Value("${jwt.secret.key}") String secretKey) {
        encodedKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // JWT access 토큰 생성
    public String createAccessToken(Member member) {
        // claim : id, nickname
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", member.getId());
        claims.put("nickname", member.getNickname());

        // 발행시간
        Date now = new Date();

        // access 토큰 발행
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenValidTime))
                .signWith(SignatureAlgorithm.HS256, encodedKey)
                .compact();
    }

    // JWT refresh 토큰 생성 (access token 재발행용)
    public String createRefreshToken() {

        // 발행시간
        Date now = new Date();

        // refresh 토큰 발행
        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshTokenValidTime))
                .signWith(SignatureAlgorithm.HS256, encodedKey)
                .compact();
    }

    // 토큰 검증 및 검증에 성공할 경우 claim 값 반환
    public Jwt<Header, Claims> verifySignature(String token) {

        try {
            return Jwts.parser().setSigningKey(encodedKey)
                    .parseClaimsJwt(token);
        } catch (ExpiredJwtException e) {
            throw new UnAuthorizedException(AuthErrorCode.EXPIRED);
        } catch (UnsupportedJwtException e) {
            throw new UnAuthorizedException(AuthErrorCode.UNSUPPORTED);
        }
    }

    public Object getMemberId(String token) {
        Jwt<Header, Claims> headerClaimsJwt = Jwts.parser().setSigningKey(encodedKey)
                .parseClaimsJwt(token);
        return headerClaimsJwt.getBody().get("id", Long.class);
    }
}
