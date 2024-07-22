package com.snowflakes.rednose.controller;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.snowflakes.rednose.entity.Member;
import com.snowflakes.rednose.repository.MemberRepository;
import com.snowflakes.rednose.service.auth.JwtTokenProvider;
import com.snowflakes.rednose.support.fixture.MemberFixture;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class AuthControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private MemberRepository memberRepository;


    @DisplayName("토큰을 재발급 받을 수 있다")
    @Test
    void 토큰_재발급성공() {
        // given
        String refreshToken = jwtTokenProvider.createRefreshToken();
        Member 지담 = MemberFixture.builder().id(1L).build();

        when(memberRepository.findById(지담.getId())).thenReturn(Optional.of(지담));
        when(memberRepository.findByRefreshToken(refreshToken)).thenReturn(Optional.of(지담));

        webTestClient.post().uri("/api/v1/reissue/kakao")
                .cookie("refreshToken", refreshToken)
                .exchange()
                .expectStatus().isFound()
                .expectHeader().value("accessToken", accessToken -> {
                    assertThat(jwtTokenProvider.getMemberId(accessToken)).isEqualTo(지담.getId());
                });
    }
}