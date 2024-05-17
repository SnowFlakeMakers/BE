package com.snowflakes.rednose.dto.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResultResponse {
    private String accessToken;

    public static LoginResultResponse from(IssueTokenResult issueTokenResult) {
        return LoginResultResponse.builder().accessToken(issueTokenResult.getAccessToken()).build();
    }
}
