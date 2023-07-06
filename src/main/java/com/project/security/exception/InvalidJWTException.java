package com.project.security.exception;

public class InvalidJWTException extends Exception {
    public InvalidJWTException(String message){
        super(message);
    }
}
