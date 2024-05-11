package com.snowflakes.rednose.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginResultResponse {

    private Long id;

    private String accessToken;
}
