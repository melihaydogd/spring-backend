package com.project.backend.exception.badrequest;

import com.project.backend.exception.HttpException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class BadRequestException extends HttpException {
    public BadRequestException(String message, int code) {
        super(message, HttpStatus.BAD_REQUEST, code);
    }
}
