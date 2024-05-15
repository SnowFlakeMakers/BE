package com.snowflakes.rednose.service.auth;

import com.snowflakes.rednose.dto.auth.IssueTokenResult;
import com.snowflakes.rednose.dto.auth.KakaoToken;
import com.snowflakes.rednose.dto.auth.UserInfo;
import com.snowflakes.rednose.entity.Member;
import com.snowflakes.rednose.exception.NotFoundException;
import com.snowflakes.rednose.exception.errorcode.MemberErrorCode;
import com.snowflakes.rednose.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.Cookie.SameSite;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    @Value("${kakao.api_key}")
    private String clientId;
    private WebClient webClient = WebClient.builder().build();

    private final JwtTokenProvider jwtTokenProvider;

    private final MemberRepository memberRepository;

    /**
     * 인가 코드를 받아 카카오 인증 서버에 post 요청을 보내고 토큰을 반환한다
     *
     * @param authCode 인가 코드
     * @return KakaoToken
     */
    public KakaoToken getToken(String authCode) {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "authorization_code");
        requestBody.add("client_id", clientId);
        requestBody.add("redirect_url", "http://localhost:8080/api/v1/login/kakao");
        requestBody.add("code", authCode);

        KakaoToken kaKaoToken = webClient.post()
                .uri("https://kauth.kakao.com/oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(requestBody))
                .retrieve()
                .bodyToMono(KakaoToken.class)
                .block();

        return kaKaoToken;
    }

    public UserInfo getUserInfo(KakaoToken kakaoToken) {
        UserInfo userInfo = webClient.get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + kakaoToken.getAccess_token())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .retrieve()
                .bodyToMono(UserInfo.class)
                .block();
        return userInfo;
    }

    public Member getExistMember(UserInfo userinfo) {
        return memberRepository.findBySocialId(userinfo.getId())
                .orElseThrow(() -> new NotFoundException(MemberErrorCode.NOT_FOUND));
    }

    @Transactional
    public String issueAccessToken(Member member) {
        return jwtTokenProvider.createAccessToken(member);
    }

    @Transactional
    public String issueRefreshToken(Member member) {
        String refreshToken = jwtTokenProvider.createRefreshToken();
        member.setRefreshToken(refreshToken);
        return refreshToken;
    }

    public Member validateRefreshToken(String refreshToken) {
        Jwt<Header, Claims> headerClaimsJwt = jwtTokenProvider.verifySignature(refreshToken);
        Long memberId = headerClaimsJwt.getBody().get("id", Long.class);
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("존재하지 않는 회원"));
        if (member.getRefreshToken().equals(refreshToken)) {
            // db에 있는 refreshToken과 클라이언트가 재발급하기 위해 보낸 refreshToken이 동일한 경우
            return member;
        }
        throw new RuntimeException("Refresh Token값이 일치하지 않습니다");
    }

    public IssueTokenResult issueToken(Member member) {
        String accessToken = issueAccessToken(member);
        String refreshToken = issueRefreshToken(member);
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite(SameSite.NONE.attributeValue())
                .build();
        return IssueTokenResult.builder().accessToken(accessToken).refreshTokenCookie(refreshTokenCookie.toString())
                .build();
    }
}

