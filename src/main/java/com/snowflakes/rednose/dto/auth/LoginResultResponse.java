package com.snowflakes.rednose.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
@Getter
@Builder
public class LoginResultResponse {

    private Long id;

    private String accessToken;
}
