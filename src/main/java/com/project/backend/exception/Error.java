package com.project.backend.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Builder
@Getter
@Setter
public class Error {

    private String message;
    private int status;
    private String error;
    private int code;
    @Builder.Default
    private String timestamp = Instant.now().toString();

}
