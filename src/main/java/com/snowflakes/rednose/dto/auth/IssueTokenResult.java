package com.snowflakes.rednose.dto.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class IssueTokenResult {
    private String accessToken;
    private String refreshTokenCookie;
}
