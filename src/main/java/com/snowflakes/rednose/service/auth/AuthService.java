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
import com.snowflakes.rednose.util.RandomNicknameGenerator;
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

    public static final String REFRESH_TOKEN = "refreshToken";
    public static final String ACCESS_TOKEN = "accessToken";
    public static final String PATH = "/";
    public static final String REDIRECT_URL = "http://localhost:8080/api/v1/login/kakao";
    public static final String KAUTH_GET_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    public static final String KAUTH_GET_USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";
    public static final String IMAGE_URL = "imageUrl";
    @Value("${kakao.api_key}")
    private String clientId;
    private WebClient webClient = WebClient.builder().build();

    private final RandomNicknameGenerator randomNicknameGenerator;

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
        requestBody.add("redirect_url", REDIRECT_URL);
        requestBody.add("code", authCode);

        KakaoToken kaKaoToken = webClient.post()
                .uri(KAUTH_GET_TOKEN_URL)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(requestBody))
                .retrieve()
                .bodyToMono(KakaoToken.class)
                .block();

        return kaKaoToken;
    }

    public UserInfo getUserInfo(KakaoToken kakaoToken) {
        UserInfo userInfo = webClient.get()
                .uri(KAUTH_GET_USER_INFO_URL)
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
        member.storeRefreshToken(refreshToken);

        return buildIssueTokenResult(accessToken, refreshToken, member);
    }

    private IssueTokenResult buildIssueTokenResult(String accessToken, String refreshToken, Member member) {
        ResponseCookie refreshTokenCookie = buildRefreshTokenCookie(refreshToken);
        return IssueTokenResult.builder()
                .refreshTokenCookie(refreshTokenCookie.toString())
                .accessToken(accessToken)
                .nickname(member.getNickname())
                .image(member.getImage())
                .build();
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(MemberErrorCode.NOT_FOUND));
    }

    @Transactional
    public IssueTokenResult issueTokenWithUserInfo(UserInfo userInfo) {
        String randomNickname = randomNicknameGenerator.generate();

        Member member = memberRepository.findBySocialId(userInfo.getId())
                .orElseGet(() -> memberRepository.save(userInfo.toMember(randomNickname)));

        Long memberId = member.getId();

        String accessToken = issueAccessToken(memberId);
        String refreshToken = issueRefreshToken(memberId);

        return buildIssueTokenResult(accessToken, refreshToken, member);
    }

    private ResponseCookie buildAccessTokenCookie(String accessToken) {
        return ResponseCookie.from(ACCESS_TOKEN, accessToken)
                .httpOnly(true)
                .secure(true)
                .path(PATH)
                .sameSite(SameSite.NONE.attributeValue())
                .build();
    }

    private ResponseCookie buildRefreshTokenCookie(String refreshToken) {
        return ResponseCookie.from(REFRESH_TOKEN, refreshToken)
                .httpOnly(true)
                .secure(true)
                .path(PATH)
                .sameSite(SameSite.NONE.attributeValue())
                .build();
    }

    private ResponseCookie buildImageUrlCookie(String imageUrl) {
        return ResponseCookie.from(IMAGE_URL, imageUrl)
                .httpOnly(true)
                .secure(true)
                .path(PATH)
                .sameSite(SameSite.NONE.attributeValue())
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

    @Transactional
    public void logout(Long memberId) {
        Member member = findMemberById(memberId);
        member.expireRefreshToken();
    }
}

