package com.project.backend.exception.forbidden;


public class InvalidJWTException extends ForbiddenException {
    public InvalidJWTException() {
        super("This is not a refresh token", ForbiddenCode.InvalidJWTException.getCode());
    }
    public InvalidJWTException(String message) {
        super(message, ForbiddenCode.InvalidJWTException.getCode());
    }
}
