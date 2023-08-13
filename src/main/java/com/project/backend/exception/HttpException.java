package com.project.backend.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public abstract class HttpException extends Exception {

    private final HttpStatus status;
    private int code;

    public HttpException(String message, HttpStatus status, int code) {
        super(message);
        this.status = status;
        this.code = code;
    }

}

