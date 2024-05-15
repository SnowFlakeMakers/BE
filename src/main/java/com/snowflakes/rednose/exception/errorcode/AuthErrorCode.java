package com.snowflakes.rednose.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AuthErrorCode implements ErrorCode {

    EXPIRED("token이 만료되었습니다"),
    UNSUPPORTED("지원하지 않는 token입니다"),
    MALFORMED("잘못된 구조의 token입니다"),
    SIGNATURE("데이터가 변조된 token입니다");

    private final String message;

}
