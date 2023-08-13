package com.project.backend.exception.forbidden;

import com.project.backend.exception.HttpException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ForbiddenException extends HttpException {
    public ForbiddenException(String message, int code) {
        super(message, HttpStatus.FORBIDDEN, code);
    }
}
