package com.snowflakes.rednose.controller;

import com.snowflakes.rednose.dto.auth.KakaoToken;
import com.snowflakes.rednose.dto.auth.LoginResultResponse;
import com.snowflakes.rednose.dto.auth.UserInfo;
import com.snowflakes.rednose.repository.MemberRepository;
import com.snowflakes.rednose.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @RequestMapping("/login/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestParam String authCode) {
        // access 토큰 받기 : 인가코드를 포함한 POST 요청
        KakaoToken kakaoToken = authService.getToken(authCode);

        // 사용자 정보 가져오기 : accessToken을 포함한 GET 요청
        UserInfo userinfo = authService.getUserInfo(kakaoToken);

        // 로그인 처리
        LoginResultResponse loginResultResponse = authService.kakaoLogin(userinfo);

        return ResponseEntity.status(HttpStatus.OK).body(loginResultResponse);
    }
}
