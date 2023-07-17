package com.project.backend.controller.auth;

import com.project.backend.dto.auth.AuthenticationRequest;
import com.project.backend.dto.auth.RefreshTokenRequest;
import com.project.backend.dto.auth.RegisterRequest;
import com.project.backend.dto.auth.AuthenticationResponse;
import com.project.backend.exception.InvalidJWTException;
import com.project.backend.exception.UserAlreadyExistsException;
import com.project.backend.service.security.AuthenticationService;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Operation(
            description = "Registers a user",
            summary = "Summary for register operation",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    )
            }
    )
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @Valid @RequestBody RegisterRequest request
    ) throws UserAlreadyExistsException {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @Operation(
            description = "Logins a user",
            summary = "Summary for login operation"
    )
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @Valid @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.login(request));
    }

    @Operation(
            description = "Refreshes an access token",
            summary = "Summary for refresh token operation"
    )
    @PostMapping("/refreshToken")
    public ResponseEntity<AuthenticationResponse> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request
    ) throws ExpiredJwtException, InvalidJWTException {
        return ResponseEntity.ok(authenticationService.refreshToken(request));
    }

}
