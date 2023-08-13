package com.project.backend.exception.badrequest;

public class UserAlreadyExistsException extends BadRequestException {
    public UserAlreadyExistsException() {
        super("This email is being used", BadRequestCode.UserAlreadyExistsException.getCode());
    }
    public UserAlreadyExistsException(String message) {
        super(message, BadRequestCode.UserAlreadyExistsException.getCode());
    }
}
