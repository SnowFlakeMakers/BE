package com.snowflakes.rednose.dto.auth;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
// 아래 2개 애노테이션은 테스트를 위해 임시로 붙임
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class LoginResultResponse {
    private String imageUrl;
    private String nickname;

    public static LoginResultResponse from(IssueTokenResult issueTokenResult) {
        return LoginResultResponse.builder()
                .nickname(issueTokenResult.getNickname())
                .imageUrl(issueTokenResult.getImageUrl()).build();
    }
}
