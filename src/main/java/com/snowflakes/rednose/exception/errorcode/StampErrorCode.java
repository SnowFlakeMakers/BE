package com.snowflakes.rednose.exception.errorcode;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum StampErrorCode implements ErrorCode {

    ALREADY_EXIST("존재하는 우표입니다"),
    NOT_FOUND("존재하지 않는 우표입니다");

    private final String message;
}
