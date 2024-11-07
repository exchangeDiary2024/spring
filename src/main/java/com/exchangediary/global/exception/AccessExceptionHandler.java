package com.exchangediary.global.exception;

import com.exchangediary.global.exception.serviceexception.ForbiddenException;
import com.exchangediary.global.exception.serviceexception.NotFoundException;
import com.exchangediary.global.exception.serviceexception.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AccessExceptionHandler {
    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Object handleForbiddenException(ForbiddenException exception, HttpServletRequest request) {
        if (request.getRequestURI().contains("/api")) {
            return makeApiErrorResponse(exception);
        }
        return "error/403";
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Object handleNotFoundException(NotFoundException exception, HttpServletRequest request) {
        if (request.getRequestURI().contains("/api")) {
            return makeApiErrorResponse(exception);
        }
        return "error/404";
    }

    private ResponseEntity<ApiErrorResponse> makeApiErrorResponse(ServiceException exception) {
        return ResponseEntity
                .status(exception.getErrorCode().getStatusCode())
                .body(ApiErrorResponse.from(exception));
    }
}
