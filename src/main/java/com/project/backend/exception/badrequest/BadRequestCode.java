package com.project.backend.exception.badrequest;

import lombok.Getter;

@Getter
public enum BadRequestCode {
    // Bad request codes starts at 1000
    UserAlreadyExistsException(1000),
    BadCredentialsException(1001),
    BindException(1002);

    private final int code;

    BadRequestCode(int code) {
        this.code = code;
    }

}
