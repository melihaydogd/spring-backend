package com.project.security.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.security.exception.Error;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class WriteErrorResponse {

    private final ObjectMapper objectMapper;

    public void writeErrorResponse(String title, String info, HttpStatus httpStatus, int code, HttpServletResponse response) throws IOException {
        Error err = Error.builder()
                .title(title)
                .info(info)
                .status(httpStatus.value())
                .error(httpStatus.getReasonPhrase())
                .code(code)
                .build();

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(httpStatus.value());
        objectMapper.writeValue(response.getOutputStream(), err);
    }

}
