package com.snowflakes.rednose.controller;


import com.snowflakes.rednose.dto.error.ErrorResponse;
import com.snowflakes.rednose.exception.CustomException;
import com.snowflakes.rednose.exception.InternalServerException;
import com.snowflakes.rednose.exception.errorcode.ErrorCode;
import com.snowflakes.rednose.exception.errorcode.InternalServerErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllException(Exception e) {
        log.info(e.getMessage());
        return handleExceptionInternal(new InternalServerException(InternalServerErrorCode.ERROR));
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleCustomException(CustomException e) {
        return handleExceptionInternal(e);
    }

    private ResponseEntity<Object> handleExceptionInternal(CustomException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(makeErrorResponse(e.getErrorCode()));
    }

    private ErrorResponse makeErrorResponse(ErrorCode errorCode) {
        return ErrorResponse.builder().code(errorCode.name()).message(errorCode.getMessage()).build();
    }
}
