package com.snowflakes.rednose.dto.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResultResponse {
    private String accessToken;
    private String imageUrl;
    private String nickname;

    public static LoginResultResponse from(IssueTokenResult issueTokenResult) {
        return LoginResultResponse.builder().accessToken(issueTokenResult.getAccessToken())
                .nickname(issueTokenResult.getNickname())
                .imageUrl(issueTokenResult.getImageUrl()).build();
    }
}
