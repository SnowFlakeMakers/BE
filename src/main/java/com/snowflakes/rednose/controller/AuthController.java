package com.snowflakes.rednose.controller;

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

    private final AuthServiece authServiece;

    // 로그인이자 회원가입
    @RequestMapping("/login/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestParam String authCode) {
        // 리다이렉트 : 카카오 인증 서버 -> rednose 서버 (인가코드 포함)

        // access 토큰 받기 위한 POST 요청
        KaKaoToken kaKaoToken = authServiece.getAccestoken(authCode);

        // 로그인 처리 (rednose 로직에 따라 처리 후 사용자에게 토큰 발급)
        /**
         * 토큰에서 사용자 정보를 가져와서 로직에 따라 처리한다
         * 1. 존재하지 않는 회원 -> 회원가입 처리 후 access token 반환
         * 2. 존재하는 회원 -> 로그인 처리 후 access token 반환
         */
        return ResponseEntity.status(HttpStatus.OK).body("로그인 결과(토큰. 닉네임 등)");
    }

    // 로그아웃
}
