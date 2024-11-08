package com.exchangediary.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class RequestValidationExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleInvalidArgumentException(MethodArgumentNotValidException exception) {
        try {
            ApiErrorResponse response = ApiErrorResponse.from(Objects.requireNonNull(exception.getFieldError()));
            log.error("{}", String.format("%s", exception.getFieldError().getDefaultMessage()));
            return response;
        } catch (NullPointerException e) {
            return ApiErrorResponse.from(HttpStatus.BAD_REQUEST);
        }
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleMissingParameterException(MissingServletRequestParameterException exception) {
        log.error("{}", String.format("%s", exception.getMessage()));
        return ApiErrorResponse.from(exception);
    }
}
