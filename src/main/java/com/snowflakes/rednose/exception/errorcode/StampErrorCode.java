package com.snowflakes.rednose.exception.errorcode;

public enum StampErrorCode implements ErrorCode {

    NOT_FOUND("우표를 찾을 수 없습니다.");

    private final String message;

    StampErrorCode(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
