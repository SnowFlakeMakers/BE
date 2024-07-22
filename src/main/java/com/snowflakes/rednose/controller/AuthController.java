package com.snowflakes.rednose.controller;

import com.snowflakes.rednose.annotation.AccessibleWithoutLogin;
import com.snowflakes.rednose.annotation.MemberId;
import com.snowflakes.rednose.dto.auth.IssueTokenResult;
import com.snowflakes.rednose.dto.auth.UserInfo;
import com.snowflakes.rednose.service.auth.AuthService;
import java.net.URLEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {

    public static final String REDIRECT_URL_FORMAT = "%s?nickname=%s?image=%s";

    private final AuthService authService;
    private final String FRONT_HOMEPAGE = "http://localhost:3000/home";

    @AccessibleWithoutLogin
    @GetMapping("/login/kakao")
    public ResponseEntity<Void> kakaoLogin(@RequestParam String code) {
        UserInfo userInfo = authService.getUserInfoFromAuthCode(code);
        IssueTokenResult issueTokenResult = authService.issueTokenWithUserInfo(userInfo);
        return buildLoginResultResponse(issueTokenResult);
    }

    private ResponseEntity<Void> buildLoginResultResponse(IssueTokenResult issueTokenResult) {
        String REDIRECT_URL = String.format(REDIRECT_URL_FORMAT, FRONT_HOMEPAGE,
                URLEncoder.encode(issueTokenResult.getNickname()), URLEncoder.encode(issueTokenResult.getImage()));
        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, REDIRECT_URL)
                .header(HttpHeaders.SET_COOKIE,
                        issueTokenResult.getRefreshTokenCookie())
                .header("accessToken", issueTokenResult.getAccessToken())
                .build();
    }

    @AccessibleWithoutLogin
    @PostMapping("/reissue/kakao")
    public ResponseEntity<Void> kakaoReissue(
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
}
