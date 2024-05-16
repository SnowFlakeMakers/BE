package com.snowflakes.rednose.exception.errorcode;

public enum MemberErrorCode implements ErrorCode{

    NOT_FOUND("회원을 찾을 수 없습니다.");

    private final String message;

    MemberErrorCode(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
