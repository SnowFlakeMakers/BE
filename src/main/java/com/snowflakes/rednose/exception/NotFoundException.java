package com.snowflakes.rednose.exception;

import com.snowflakes.rednose.exception.errorcode.ErrorCode;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NotFoundException extends CustomException {
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode, HttpStatus.NOT_FOUND);
    }
}
