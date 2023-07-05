package com.project.security.service.security;

import com.project.security.util.WriteErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthenticationEntryPointService implements AuthenticationEntryPoint {

    private final WriteErrorResponse writeErrorResponse;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        writeErrorResponse.writeErrorResponse("Authentication Denied", authException.getMessage(), HttpStatus.UNAUTHORIZED, 1002, response);
    }

}
