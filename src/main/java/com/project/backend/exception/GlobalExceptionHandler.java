package com.project.backend.exception;

import com.project.backend.exception.badrequest.BadRequestCode;
import com.project.backend.exception.badrequest.BadRequestException;
import com.project.backend.exception.badrequest.UserAlreadyExistsException;
import com.project.backend.exception.forbidden.InvalidJWTException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error> handleException(Exception e) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return new ResponseEntity<>(
                Error.builder()
                        .message(String.format("Exception Class: %s, Exception Message: %s", e.getClass(), e.getMessage()))
                        .status(status.value())
                        .error(status.getReasonPhrase())
                        .code(1)
                        .build(),
                status
        );
    }

    @ExceptionHandler(HttpException.class)
    public ResponseEntity<Error> handleException(HttpException e) {
        return this.createError(e);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Error> handleException(BadCredentialsException e) {
        return this.handleException(new BadRequestException(e.getMessage(), BadRequestCode.BadCredentialsException.getCode()));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<Error> handleException(BindException e) {
        String message = e.getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(","));
        return this.handleException(new BadRequestException(message, BadRequestCode.BindException.getCode()));
    }

    private ResponseEntity<Error> createError(HttpException e) {
        return new ResponseEntity<>(
                Error.builder()
                        .message(e.getMessage())
                        .status(e.getStatus().value())
                        .error(e.getStatus().getReasonPhrase())
                        .code(e.getCode())
                        .build(),
                e.getStatus()
        );
    }

}
