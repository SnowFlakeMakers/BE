package com.snowflakes.rednose.exception.errorcode;

public enum StampLikeErrorCode implements ErrorCode {

    NOT_FOUND("해당 우표의 좋아요 기록을 찾을 수 없습니다."),

    ALREADY_EXIST("이미 좋아요를 누른 우표입니다.");

    private final String message;

    StampLikeErrorCode(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
