package com.snowflakes.rednose.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SealErrorCode implements ErrorCode {

    ALREADY_EXIST("존재하는 씰입니다"),
    NOT_FOUND("존재하지 않는 씰입니다");

    private final String message;
}
