package com.snowflakes.rednose.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum InternalServerErrorCode implements ErrorCode {

    ERROR("서버 내부 에러");

    private final String message;

}
