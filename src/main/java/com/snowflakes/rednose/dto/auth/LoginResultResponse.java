package com.snowflakes.rednose.dto.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResultResponse {
    private String accessToken;
    private String imageUrl;

    public static LoginResultResponse from(IssueTokenResult issueTokenResult) {
        return LoginResultResponse.builder().accessToken(issueTokenResult.getAccessToken())
                .imageUrl(issueTokenResult.getImageUrl()).build();
    }
}
