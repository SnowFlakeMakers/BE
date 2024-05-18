package com.snowflakes.rednose.exception.errorcode;

public enum SealLikeErrorCode implements ErrorCode{

    ALREADY_EXIST("이미 좋아요한 씰입니다.");

    private final String message;

    SealLikeErrorCode(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
