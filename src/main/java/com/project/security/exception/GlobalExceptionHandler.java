package com.project.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error> handleException(Exception e) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return new ResponseEntity<>(
                Error.builder()
                        .title("Internal Error")
                        .info(String.format("Exception Class: %s, Exception Message: %s", e.getClass(), e.getMessage()))
                        .status(status.value())
                        .error(status.getReasonPhrase())
                        .code(1000)
                        .build(),
                status
        );
    }

    @ExceptionHandler(InvalidJWTException.class)
    public ResponseEntity<Error> handleInvalidJWTException(InvalidJWTException e) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        return new ResponseEntity<>(
                Error.builder()
                        .title("Invalid JWT")
                        .info(e.getMessage())
                        .status(status.value())
                        .error(status.getReasonPhrase())
                        .code(1004)
                        .build(),
                status
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Error> handleBadCredentialsException(BadCredentialsException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(
                Error.builder()
                        .title("Login Failed")
                        .info(e.getMessage())
                        .status(status.value())
                        .error(status.getReasonPhrase())
                        .code(1005)
                        .build(),
                status
        );
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<Error> handleBindException(BindException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(
                Error.builder()
                        .title("Invalid Argument")
                        .info(e.getAllErrors().get(0).getDefaultMessage())
                        .status(status.value())
                        .error(status.getReasonPhrase())
                        .code(1006)
                        .build(),
                status
        );
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Error> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(
                Error.builder()
                        .title("User Exists")
                        .info(e.getMessage())
                        .status(status.value())
                        .error(status.getReasonPhrase())
                        .code(1007)
                        .build(),
                status
        );
    }

}
