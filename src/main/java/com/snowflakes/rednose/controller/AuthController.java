package com.snowflakes.rednose.controller;

import com.snowflakes.rednose.annotation.AccessibleWithoutLogin;
import com.snowflakes.rednose.dto.auth.IssueTokenResult;
import com.snowflakes.rednose.dto.auth.LoginResultResponse;
import com.snowflakes.rednose.dto.auth.UserInfo;
import com.snowflakes.rednose.entity.Member;
import com.snowflakes.rednose.service.MemberService;
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
    private final MemberService memberService;

    @AccessibleWithoutLogin
    @GetMapping("/login/kakao")
    public ResponseEntity<LoginResultResponse> kakaoLogin(@RequestParam String code) {
        UserInfo userInfo = authService.getUserInfoFromAuthCode(code);

        // 회원가입 한 사용자인지 확인 (아닐 경우 -> 에러 던짐 : 닉네임을 정하게 해야함)
        Member member = memberService.getExistMember(userInfo);

        // 회원가입 한 사용자 -> 로그인처리, 토큰을 발급
        IssueTokenResult issueTokenResult = authService.issueToken(member);

        // 클라이언트에게 응답 (헤더 쿠키 : refreshToken, 바디 : accessToken)
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, issueTokenResult.getRefreshTokenCookie())
                .body(LoginResultResponse.builder().accessToken(issueTokenResult.getAccessToken()).build());
    }

    @AccessibleWithoutLogin
    @PostMapping("/reissue/kakao")
    public ResponseEntity<LoginResultResponse> kakaoReissue(
            @CookieValue("refreshToken") String refreshToken) {
        // 리프레시 토큰 -> db에 있는 것과 일치하는지 검증. 일치한다면 해당 member를 반환한다 (토큰을 발급하기 위해)
        Member member = authService.validateRefreshToken(refreshToken);

        // 로그인 처리 및 토큰 발급
        IssueTokenResult issueTokenResult = authService.issueToken(member);

        // 응답 (헤더 쿠키 : refreshToken, 바디 : accessToken)
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, issueTokenResult.getRefreshTokenCookie())
                .body(LoginResultResponse.from(issueTokenResult));
    }
}
