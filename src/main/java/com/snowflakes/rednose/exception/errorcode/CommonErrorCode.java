package com.snowflakes.rednose.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CommonErrorCode implements ErrorCode {

    INVALID_INPUT("잘못된 입력값입니다"),
    NOT_READABLE("잘못된 형식입니다");

    private final String message;

}
