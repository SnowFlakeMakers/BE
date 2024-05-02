package com.snowflakes.rednose.exception;

public enum ErrorCode {

    MEMBER_NOT_FOUND("회원을 찾을 수 없습니다."),

    STAMP_NOT_FOUND("우표를 찾을 수 없습니다.");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
