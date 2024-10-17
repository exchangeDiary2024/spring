package com.exchangediary.global.exception;

import com.exchangediary.global.exception.serviceexception.ConfilctException;
import com.exchangediary.global.exception.serviceexception.FailedImageUploadException;
import com.exchangediary.global.exception.serviceexception.KakaoLoginFailureException;
import com.exchangediary.global.exception.serviceexception.DuplicateException;
import com.exchangediary.global.exception.serviceexception.ServiceException;
import com.exchangediary.global.exception.serviceexception.InvalidDateException;
import com.exchangediary.global.exception.serviceexception.NotFoundException;
import com.exchangediary.global.exception.serviceexception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

import static com.exchangediary.global.exception.ErrorCode.IMAGE_SIZE_TOO_LARGE;
import static com.exchangediary.global.exception.ErrorCode.INVALID_IMAGE_FORMAT;

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

    @ExceptionHandler({InvalidDateException.class, DuplicateException.class})
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleInvalidRangeException(ServiceException exception) {
        return ApiErrorResponse.from(exception.getErrorCode(), exception.getMessage(), exception.getValue());
    }

    @ExceptionHandler({UnauthorizedException.class})
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public ApiErrorResponse handleUnauthorizedException(ServiceException exception) {
        return ApiErrorResponse.from(exception.getErrorCode(), exception.getMessage(), exception.getValue());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ApiErrorResponse handleNotFoundException(ServiceException exception) {
        return ApiErrorResponse.from(exception.getErrorCode(), exception.getMessage(), exception.getValue());
    }

    @ExceptionHandler({KakaoLoginFailureException.class})
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiErrorResponse handleKakaoLoginException(ServiceException exception) {
        return ApiErrorResponse.from(exception.getErrorCode(), exception.getMessage(), exception.getValue());
    }

    @ExceptionHandler({ConfilctException.class})
    @ResponseStatus(code = HttpStatus.CONFLICT)
    public ApiErrorResponse handleConfilctException(ServiceException exception) {
        return ApiErrorResponse.from(exception.getErrorCode(), exception.getMessage(), exception.getValue());
    }

    @ExceptionHandler({FailedImageUploadException.class})
    public ResponseEntity<ApiErrorResponse> handleFailedImageUploadException(FailedImageUploadException exception) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorCode errorCode = exception.getErrorCode();

        if (errorCode == IMAGE_SIZE_TOO_LARGE) {
            status = HttpStatus.PAYLOAD_TOO_LARGE;
        }
        if (errorCode == INVALID_IMAGE_FORMAT) {
            status = HttpStatus.UNSUPPORTED_MEDIA_TYPE;
        }
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.from(errorCode, errorCode.getMessage(), exception.getValue());

        return new ResponseEntity<>(apiErrorResponse, status);
    }
}
