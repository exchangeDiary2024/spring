package com.exchangediary.global.exception;

import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MissingServletRequestParameterException;

@Builder
public record ApiErrorResponse(
        int statusCode,
        String message,
        String value
) {
    public static ApiErrorResponse from(FieldError fieldError) {
        return ApiErrorResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(String.format("%s %s", fieldError.getField(), fieldError.getDefaultMessage()))
                .value((String) fieldError.getRejectedValue())
                .build();
    }

    public static ApiErrorResponse from(HttpStatus status) {
        return ApiErrorResponse.builder()
                .statusCode(status.value())
                .build();
    }

    public static ApiErrorResponse from(MissingServletRequestParameterException exception) {
        return ApiErrorResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(exception.getMessage())
                .build();
    }

    public static ApiErrorResponse from(ErrorCode errorCode, String message, String value) {
        return ApiErrorResponse.builder()
                .statusCode(errorCode.getStatusCode().value())
                .message(message)
                .value(value)
                .build();
    }
}
