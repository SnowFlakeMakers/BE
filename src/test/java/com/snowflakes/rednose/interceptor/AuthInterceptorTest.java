package com.snowflakes.rednose.interceptor;

import com.snowflakes.rednose.exception.errorcode.AuthErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class AuthInterceptorTest {

    @Autowired
    private WebTestClient webTestClient;

    @DisplayName("토큰 값이 null 일 경우 알맞은 예외를 던진다")
    @Test
    void 토큰값널_예외() {
        webTestClient.post()
                .uri("/api/v1/seals")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "") // 빈 Authorization 헤더를 설정
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .jsonPath("$.code").isEqualTo(AuthErrorCode.NULL_OR_BLANK_TOKEN.toString())
                .jsonPath("$.message").isEqualTo(AuthErrorCode.NULL_OR_BLANK_TOKEN.getMessage());
    }
}