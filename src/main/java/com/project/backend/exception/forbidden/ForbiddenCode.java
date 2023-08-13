package com.project.backend.exception.forbidden;

import lombok.Getter;

@Getter
public enum ForbiddenCode {
    // Forbidden codes starts at 2000
    InvalidJWTException(2000);

    private final int code;

    ForbiddenCode(int code) {
        this.code = code;
    }

}
