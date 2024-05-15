package com.snowflakes.rednose.controller;


import com.snowflakes.rednose.dto.error.ErrorResponse;
import com.snowflakes.rednose.dto.error.ErrorResponse.ValidationError;
import com.snowflakes.rednose.exception.CustomException;
import com.snowflakes.rednose.exception.InternalServerException;
import com.snowflakes.rednose.exception.errorcode.CommonErrorCode;
import com.snowflakes.rednose.exception.errorcode.ErrorCode;
import com.snowflakes.rednose.exception.errorcode.InternalServerErrorCode;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllException(Exception e) {
        log.info(e.getMessage());
        return handleExceptionInternal(new InternalServerException(InternalServerErrorCode.ERROR));
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleCustomException(CustomException e) {
        return handleExceptionInternal(e);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatusCode status,
                                                                  WebRequest request) {
        return handleExceptionInternal(ex);
    }

    private ResponseEntity<Object> handleExceptionInternal(HttpMessageNotReadableException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(makeErrorResponse(CommonErrorCode.NOT_READABLE));
    }

    private ResponseEntity<Object> handleExceptionInternal(CustomException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(makeErrorResponse(e.getErrorCode()));
    }

    private ErrorResponse makeErrorResponse(ErrorCode errorCode) {
        return ErrorResponse.builder().code(errorCode.name()).message(errorCode.getMessage()).build();
    }


    // 이하 valid 예외 처리
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status,
                                                                  WebRequest request) {
        return handleExceptionInternal(ex, CommonErrorCode.INVALID_INPUT);
    }

    private ResponseEntity<Object> handleExceptionInternal(BindException e, ErrorCode errorCode) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(makeErrorResponse(e, errorCode));
    }

    private ErrorResponse makeErrorResponse(BindException e, ErrorCode errorCode) {
        List<ValidationError> validationErrorList = e.getBindingResult().getFieldErrors().stream()
                .map(ErrorResponse.ValidationError::of).collect(Collectors.toList());

        return ErrorResponse.builder().code(errorCode.name()).message(errorCode.getMessage())
                .errors(validationErrorList).build();
    }
}
