package com.exchangediary.global.exception;

import com.exchangediary.global.exception.serviceexception.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleInvalidArgumentException(MethodArgumentNotValidException exception) {
        try {
            return ApiErrorResponse.from(Objects.requireNonNull(exception.getFieldError()));
        } catch (NullPointerException e) {
            return ApiErrorResponse.from(HttpStatus.BAD_REQUEST);
        }
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleMissingParameterException(MissingServletRequestParameterException exception) {
        return ApiErrorResponse.from(exception);
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ApiErrorResponse> handleServiceException(ServiceException exception) {
        ApiErrorResponse body = ApiErrorResponse.from(
                exception.getErrorCode(),
                exception.getMessage(),
                exception.getValue()
        );
        return ResponseEntity
                .status(exception.getErrorCode().getStatusCode())
                .body(body);
    }
}
