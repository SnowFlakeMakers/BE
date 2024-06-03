package com.snowflakes.rednose.controller;

import com.snowflakes.rednose.annotation.AccessibleWithoutLogin;
import com.snowflakes.rednose.annotation.MemberId;
import com.snowflakes.rednose.dto.auth.IssueTokenResult;
import com.snowflakes.rednose.dto.auth.LoginResultResponse;
import com.snowflakes.rednose.dto.auth.UserInfo;
import com.snowflakes.rednose.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 프론트가 아직 배포되지 않아 백으로 리다이렉트하고 정상적으로 리다이렉트되는지 확인하기 위함. 프론트 배포 후 url 수정 예정
    private final String FRONT_HOMEPAGE = "http://localhost:8080/api/v1/redirectTest";


    @AccessibleWithoutLogin
    @GetMapping("/login/kakao")
    public ResponseEntity<LoginResultResponse> kakaoLogin(@RequestParam String code) {
        UserInfo userInfo = authService.getUserInfoFromAuthCode(code);
        IssueTokenResult issueTokenResult = authService.issueTokenWithUserInfo(userInfo);
        return buildLoginResultResponse(issueTokenResult);
    }

    private ResponseEntity<LoginResultResponse> buildLoginResultResponse(IssueTokenResult issueTokenResult) {
        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.SET_COOKIE, issueTokenResult.getRefreshTokenCookie(),
                        issueTokenResult.getAccessTokenCookie())
                .header(HttpHeaders.LOCATION, FRONT_HOMEPAGE)
                .body(LoginResultResponse.from(issueTokenResult));
    }

    @AccessibleWithoutLogin
    @PostMapping("/reissue/kakao")
    public ResponseEntity<LoginResultResponse> kakaoReissue(
            @CookieValue("refreshToken") String refreshToken) {
        IssueTokenResult issueTokenResult = authService.reIssueToken(refreshToken);
        return buildLoginResultResponse(issueTokenResult);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(@MemberId Long memberId) {
        authService.logout(memberId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @AccessibleWithoutLogin
    @GetMapping("/test")
    public String test(@RequestParam(defaultValue = "1") Long memberId) {
        return authService.issueAccessToken(memberId);
    }


    // 리다이렉트 테스트용 임시 메서드입니다
    @AccessibleWithoutLogin
    @GetMapping("/redirectTest")
    public String redirectTest(@RequestHeader("accessToken") String accessToken,
                               @RequestHeader("refreshToken") String refreshToken,
                               @RequestBody LoginResultResponse loginResultResponse) {
        log.info("redirect successful");
        String result = String.format("accessToken : %s \nrefreshToken : %s\nnickname : %s\nimageUrl : %s", accessToken,
                refreshToken, loginResultResponse.getNickname(), loginResultResponse.getImageUrl());
        return result;
    }
}
