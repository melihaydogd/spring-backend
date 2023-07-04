package com.project.security.service.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.security.dto.request.AuthenticationRequest;
import com.project.security.dto.request.RegisterRequest;
import com.project.security.dto.response.AuthenticationResponse;
import com.project.security.model.token.Token;
import com.project.security.model.token.TokenRepository;
import com.project.security.model.token.TokenType;
import com.project.security.model.user.Role;
import com.project.security.model.user.User;
import com.project.security.model.user.UserRepository;
import com.project.security.util.WriteErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final WriteErrorResponse writeErrorResponse;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user);
        String[] jwtTokens = this.generateTokens(user, null);
        return generateResponse(jwtTokens);
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        revokeAllUserTokens((User) authenticate.getPrincipal());
        String[] jwtTokens = this.generateTokens((User) authenticate.getPrincipal(), null);
        return generateResponse(jwtTokens);
    }

    public AuthenticationResponse refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (Objects.isNull(authHeader) || !authHeader.startsWith("Bearer ")) {
            return null;
        }

        final String refreshToken = authHeader.substring(7);
        final String email;
        try {
            email = jwtService.extractEmail(refreshToken);
        } catch (ExpiredJwtException exception) {
            writeErrorResponse.writeErrorResponse("Expired JWT", exception.getMessage(), HttpStatus.UNAUTHORIZED, response);
            return null;
        }

        if (Objects.nonNull(email)) {
            var userDetails = this.userRepository.findByEmail(email).orElseThrow();
            if (jwtService.isTokenValid(refreshToken, userDetails)) {
                revokeAllUserTokens(userDetails);
                String[] jwtTokens = this.generateTokens(userDetails, refreshToken);
                return generateResponse(jwtTokens);
            } else {
                writeErrorResponse.writeErrorResponse("Invalid JWT", "Emails do not match", HttpStatus.UNAUTHORIZED, response);
            }
        }
        return null;
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());
        if (validUserTokens.isEmpty()) return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    private String[] generateTokens(User user, String refreshToken) {
        var jwtAccessToken = jwtService.generateToken(Map.of(), user);
        var jwtRefreshToken = Objects.isNull(refreshToken) ? jwtService.generateRefreshToken(user) : refreshToken;
        var token = Token.builder()
                .user(user)
                .token(jwtAccessToken)
                .tokenType(TokenType.BEARER)
                .build();
        tokenRepository.save(token);
        return new String[]{jwtAccessToken, jwtRefreshToken};
    }

    private AuthenticationResponse generateResponse(String[] jwtTokens) {
        return AuthenticationResponse.builder()
                .accessToken(jwtTokens[0])
                .refreshToken(jwtTokens[1])
                .build();
    }

}