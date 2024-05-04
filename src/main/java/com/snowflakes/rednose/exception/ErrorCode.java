package com.snowflakes.rednose.exception;

public enum ErrorCode {

    MEMBER_NOT_FOUND("회원을 찾을 수 없습니다."),
    STAMP_NOT_FOUND("우표를 찾을 수 없습니다."),
    STAMP_LIKE_NOT_FOUND("해당 우표의 좋아요 기록을 찾을 수 없습니다."),

    STAMP_LIKE("이미 좋아요를 누른 우표입니다.");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
