package com.snowflakes.rednose.argumentresolver;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.snowflakes.rednose.entity.Member;
import com.snowflakes.rednose.repository.MemberRepository;
import com.snowflakes.rednose.service.MemberService;
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
class MemberIdArgumentResolverTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private MemberService memberService;

    @Test
    @DisplayName("accessToken의 claim에 해당하는 id 값을 argumentResolver가 제대로 얻어서 바인딩 해줄 수 있다")
    public void testCancelMembership() {

        // given
        final Long MEMBER_ID = 10L;
        final String ACCESS_TOKEN = jwtTokenProvider.createAccessToken(MEMBER_ID);
        final Member 지담 = MemberFixture.builder().id(MEMBER_ID).build();

        doNothing().when(memberService).delete(MEMBER_ID);
        doNothing().when(memberRepository).delete(지담);
        when(memberRepository.findById(MEMBER_ID)).thenReturn(Optional.of(지담));

        // when
        webTestClient.delete().uri("/api/v1/members")
                .cookie("accessToken", ACCESS_TOKEN)
                .exchange()
                .expectStatus().isNoContent();

        // then
        verify(memberService, times(1)).delete(MEMBER_ID);
        verify(memberService, times(0)).delete(2L);
    }
}