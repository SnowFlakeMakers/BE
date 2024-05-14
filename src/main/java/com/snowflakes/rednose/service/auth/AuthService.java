package com.snowflakes.rednose.service.auth;

import com.snowflakes.rednose.dto.auth.KakaoToken;
import com.snowflakes.rednose.dto.auth.LoginResultResponse;
import com.snowflakes.rednose.dto.auth.UserInfo;
import com.snowflakes.rednose.entity.Member;
import com.snowflakes.rednose.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
        requestBody.add("code", "authCode");

        KakaoToken kaKaoToken = webClient.post()
                .uri("https://kapi.kakao.com/v2/user/me")
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

    public LoginResultResponse kakaoLogin(UserInfo userinfo) {
        // socialId로 member를 찾는다. 회원가입 하지 않은 member일 경우, 닉네임을 정하고 회원가입을 해야 되기 때문에 예외(임시)를 던진다
        Member member = memberRepository.findBySocialId(userinfo.getSocialId())
                .orElseThrow(() -> new RuntimeException("회원가입 하지 않은 사용자"));

        // 존재하는 member일 경우 accessToken과 refreshToken을 만든다. refreshToken은 db에 저장한다
        // 클라이언트에게 refresh token, access token 반환
        return issueToken(member);
    }

    /**
     * member 엔티티의 id를 통해 accessToken과 refreshToken을 만든다. refreshToken은 db에 저장한다
     *
     * @param member
     * @return
     */
    @Transactional
    public LoginResultResponse issueToken(Member member) {
        String refreshToken = jwtTokenProvider.createRefreshToken();
        String accessToken = jwtTokenProvider.createAccessToken(member);
        member.setRefreshToken(refreshToken);
        return LoginResultResponse.builder().id(member.getId()).refreshToken(refreshToken).accessToken(accessToken)
                .build();
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
}

