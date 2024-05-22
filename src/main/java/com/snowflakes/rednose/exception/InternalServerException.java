package com.snowflakes.rednose.exception;

import com.snowflakes.rednose.exception.CustomException;
import com.snowflakes.rednose.exception.errorcode.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InternalServerException extends CustomException {
    public InternalServerException(ErrorCode errorCode) {
        super(errorCode, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
