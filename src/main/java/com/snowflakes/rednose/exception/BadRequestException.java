package com.snowflakes.rednose.exception;

import com.snowflakes.rednose.exception.errorcode.ErrorCode;

public class BadRequestException extends CustomException{

    public BadRequestException(ErrorCode errorCode) {
        super(errorCode);
    }

}
