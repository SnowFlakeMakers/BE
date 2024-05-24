package com.snowflakes.rednose.service.auth;

import com.snowflakes.rednose.dto.auth.IssueTokenResult;
import com.snowflakes.rednose.dto.auth.KakaoToken;
import com.snowflakes.rednose.dto.auth.UserInfo;
import com.snowflakes.rednose.entity.Member;
import com.snowflakes.rednose.exception.NotFoundException;
import com.snowflakes.rednose.exception.UnAuthorizedException;
import com.snowflakes.rednose.exception.errorcode.AuthErrorCode;
import com.snowflakes.rednose.exception.errorcode.MemberErrorCode;
import com.snowflakes.rednose.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + kakaoToken.getAccessToken())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .retrieve()
                .bodyToMono(UserInfo.class)
                .block();
        return userInfo;
    }

    public String issueAccessToken(Long memberId) {
        return jwtTokenProvider.createAccessToken(memberId);
    }

    @Transactional
    public String issueRefreshToken(Long memberId) {
        String refreshToken = jwtTokenProvider.createRefreshToken();
        Member member = findMemberById(memberId);
        member.storeRefreshToken(refreshToken);
        return refreshToken;
    }

    public Long validateRefreshToken(String refreshToken) {
        // refresh token 검증 (refresh token이 만료되거나 구조에 문제가 있으면 이 라인에서 예외를 던짐)
        jwtTokenProvider.verifySignature(refreshToken);

        // refresh token이 유효하다면 refresh token으로 회원을 찾음
        return memberRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new UnAuthorizedException(AuthErrorCode.NOT_EXISTS_IN_DATABASE)).getId();
    }

    @Transactional
    public IssueTokenResult issueToken(Long memberId) {
        String accessToken = issueAccessToken(memberId);
        String refreshToken = issueRefreshToken(memberId);
        Member member = findMemberById(memberId);
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite(SameSite.NONE.attributeValue())
                .build();
        return IssueTokenResult.builder().accessToken(accessToken).refreshTokenCookie(refreshTokenCookie.toString())
                .nickname(member.getNickname())
                .imageUrl(member.getImage())
                .build();
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(MemberErrorCode.NOT_FOUND));
    }

    @Transactional
    public IssueTokenResult issueTokenWithUserInfo(UserInfo userInfo) {
        log.info("userinfo socialid : {}", userInfo.getId());

        Member member = memberRepository.findBySocialId(userInfo.getId())
                .orElseGet(() -> memberRepository.save(userInfo.toMember()));

        Long memberId = member.getId();

        String accessToken = issueAccessToken(memberId);
        String refreshToken = issueRefreshToken(memberId);

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite(SameSite.NONE.attributeValue())
                .build();
        return IssueTokenResult.builder().accessToken(accessToken).refreshTokenCookie(refreshTokenCookie.toString())
                .imageUrl(member.getImage())
                .nickname(member.getNickname())
                .build();
    }

    public UserInfo getUserInfoFromAuthCode(String authCode) {
        KakaoToken token = getToken(authCode);
        return getUserInfo(token);
    }

    public IssueTokenResult reIssueToken(String refreshToken) {
        Long memberId = validateRefreshToken(refreshToken);
        return issueToken(memberId);
    }
}

