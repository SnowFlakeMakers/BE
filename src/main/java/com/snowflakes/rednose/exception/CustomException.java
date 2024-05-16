package com.snowflakes.rednose.exception;

import com.snowflakes.rednose.exception.errorcode.ErrorCode;

public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;

    protected CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    protected CustomException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }

    protected ErrorCode getErrorCode() {
        return errorCode;
    }
}
