package com.snowflakes.rednose.dto.auth;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class IssueTokenResult {
    private String accessTokenCookie;
    private String refreshTokenCookie;
    private String imageUrlCookie;
    private String nickname;
}
