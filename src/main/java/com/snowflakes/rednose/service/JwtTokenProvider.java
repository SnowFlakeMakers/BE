package com.snowflakes.rednose.service;

import com.snowflakes.rednose.entity.Member;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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


    private String secretKey;

    private final long accessTokenValidTime = 30 * 60 * 1000L;
    private final long MILLISECONDS_PER_WEEK = 7 * 24 * 60 * 60 * 1000L;
    private final long refreshTokenValidTime = 2 * MILLISECONDS_PER_WEEK;

    // jwt key 암호화
    protected void init(@Value("${jwt.secret.key}") String secretKey) {
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
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
                .signWith(SignatureAlgorithm.HS256, secretKey)
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
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}
