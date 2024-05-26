package com.snowflakes.rednose.exception.errorcode;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum StampLikeErrorCode implements ErrorCode {

    ALREADY_EXIST("이미 좋아요한 우표입니다."),
    NOT_FOUND("존재하지 않는 좋아요입니다");

    private final String message;
}
