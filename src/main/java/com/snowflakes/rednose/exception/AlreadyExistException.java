package com.snowflakes.rednose.exception;

public class AlreadyExistException extends CustomException{

    public AlreadyExistException(ErrorCode errorCode) {
        super(errorCode);
    }

}
