package com.snowflakes.rednose.dto.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class IssueTokenResult {
    private String accessToken;
    private String refreshTokenCookie;
}
