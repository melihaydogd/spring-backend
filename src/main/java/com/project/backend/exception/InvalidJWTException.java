package com.project.backend.exception;

public class InvalidJWTException extends Exception {
    public InvalidJWTException(String message){
        super(message);
    }
}
