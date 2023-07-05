package com.project.security.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Builder
@Getter
@Setter
public class Error {

    private String title;
    private String info;
    private int status;
    private String error;
    private int code;
    @Builder.Default
    private String timestamp = Instant.now().toString();

}
