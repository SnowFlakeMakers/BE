package com.snowflakes.rednose.dto.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResultResponse {

    private Long id;
    private String accessToken;
    private String refreshToken;
}
