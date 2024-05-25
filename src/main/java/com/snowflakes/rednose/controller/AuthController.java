package com.snowflakes.rednose.controller;

import com.snowflakes.rednose.annotation.AccessibleWithoutLogin;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @AccessibleWithoutLogin
    @GetMapping("/login/kakao")
    public ResponseEntity<LoginResultResponse> kakaoLogin(@RequestParam String code) {
        UserInfo userInfo = authService.getUserInfoFromAuthCode(code);
        IssueTokenResult issueTokenResult = authService.issueTokenWithUserInfo(userInfo);
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, issueTokenResult.getRefreshTokenCookie())
                .body(LoginResultResponse.from(issueTokenResult));
    }

    @AccessibleWithoutLogin
    @PostMapping("/reissue/kakao")
    public ResponseEntity<LoginResultResponse> kakaoReissue(
            @CookieValue("refreshToken") String refreshToken) {
        IssueTokenResult issueTokenResult = authService.reIssueToken(refreshToken);
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, issueTokenResult.getRefreshTokenCookie())
                .body(LoginResultResponse.from(issueTokenResult));
    }

    @AccessibleWithoutLogin
    @GetMapping("/test")
    public String test(@RequestParam(defaultValue = "1") Long memberId) {
        return authService.issueAccessToken(memberId);
    }
}
