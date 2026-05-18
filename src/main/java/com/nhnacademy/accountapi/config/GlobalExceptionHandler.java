package com.nhnacademy.accountapi.config;

import com.nhnacademy.accountapi.exception.BaseException;
import com.nhnacademy.accountapi.exception.ErrorCode;
import com.nhnacademy.accountapi.exception.dto.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    protected ResponseEntity<ErrorResponse> handleBaseException(BaseException e) {
        ErrorCode errorCode = e.getErrorCode();

        return ResponseEntity.status(errorCode.getStatus())
                .body(new ErrorResponse(errorCode.getStatus().value(), errorCode.getCode(), errorCode.getMessage()));
    }

    @ExceptionHandler({ConstraintViolationException.class, HandlerMethodValidationException.class})
    protected ResponseEntity<ErrorResponse> handleNotValidHeaderException(Exception e) {
        ErrorCode errorCode = ErrorCode.NOT_VALID_PARAMETER;

        return ResponseEntity.status(errorCode.getStatus())
                .body(new ErrorResponse(errorCode.getStatus().value(), errorCode.getCode(), errorCode.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleNotValidBodyException(Exception e) {
        ErrorCode errorCode = ErrorCode.NOT_VALID_BODY;

        return ResponseEntity.status(errorCode.getStatus())
                .body(new ErrorResponse(errorCode.getStatus().value(), errorCode.getCode(), errorCode.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {
        ErrorCode errorCode = ErrorCode.UNHANDLED_EXCEPTION;
        log.info("Exception: {}", e.toString());

        return ResponseEntity.status(errorCode.getStatus())
                .body(new ErrorResponse(errorCode.getStatus().value(), errorCode.getCode(), errorCode.getMessage()));
    }
}
