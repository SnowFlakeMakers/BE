package com.snowflakes.rednose.exception.errorcode;

public enum StampCraftErrorCode implements ErrorCode {
    NOT_FOUND("우표 제작소를 찾을 수 없습니다.");

    private final String message;

    StampCraftErrorCode(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
