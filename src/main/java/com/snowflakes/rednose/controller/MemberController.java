package com.snowflakes.rednose.controller;

import com.snowflakes.rednose.annotation.AccessibleWithoutLogin;
import com.snowflakes.rednose.annotation.MemberId;
import com.snowflakes.rednose.dto.auth.IssueTokenResult;
import com.snowflakes.rednose.dto.auth.LoginResultResponse;
import com.snowflakes.rednose.dto.member.SignInRequest;
import com.snowflakes.rednose.entity.Member;
import com.snowflakes.rednose.service.MemberService;
import com.snowflakes.rednose.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final AuthService authService;

    @PostMapping("/members")
    public ResponseEntity<LoginResultResponse> signIn(@RequestBody @Valid SignInRequest request, @MemberId Long memberId) {
        // Member 저장
        memberService.signIn(request, memberId);

        // 로그인 처리 및 토큰 발급
        IssueTokenResult issueTokenResult = authService.issueToken(memberId);

        // 응답 (헤더 쿠키 : refreshToken, 바디 : accessToken)
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, issueTokenResult.getRefreshTokenCookie())
                .body(LoginResultResponse.builder().accessToken(issueTokenResult.getAccessToken()).build());
    }

    @DeleteMapping("/members")
    public ResponseEntity<Void> cancleMembership(@MemberId Long memberId) {
        memberService.delete(memberId);
        return ResponseEntity.noContent().build();
    }

}
