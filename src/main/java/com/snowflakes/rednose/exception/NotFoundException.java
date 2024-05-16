package com.snowflakes.rednose.exception;

import com.snowflakes.rednose.exception.errorcode.ErrorCode;

public class NotFoundException extends CustomException{
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

}
