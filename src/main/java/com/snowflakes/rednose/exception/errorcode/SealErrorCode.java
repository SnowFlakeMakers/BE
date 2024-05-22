package com.snowflakes.rednose.exception.errorcode;

public enum SealErrorCode implements ErrorCode{

    NOT_FOUND("씰을 찾을 수 없습니다.");

    private final String message;

    SealErrorCode(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
