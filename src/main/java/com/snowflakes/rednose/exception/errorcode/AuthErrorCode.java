package com.snowflakes.rednose.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AuthErrorCode implements ErrorCode {

    ACCESS_TOKEN_EXPIRED("access token이 만료되었습니다");

    private final String message;

}
