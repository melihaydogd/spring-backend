package com.project.backend.service.auth;

import com.project.backend.dto.auth.AuthenticationRequest;
import com.project.backend.dto.auth.RefreshTokenRequest;
import com.project.backend.dto.auth.RegisterRequest;
import com.project.backend.dto.auth.AuthenticationResponse;
import com.project.backend.exception.forbidden.InvalidJWTException;
import com.project.backend.exception.badrequest.UserAlreadyExistsException;
import com.project.backend.model.token.Token;
import com.project.backend.model.token.TokenRepository;
import com.project.backend.model.token.TokenType;
import com.project.backend.model.user.Role;
import com.project.backend.model.user.User;
import com.project.backend.model.user.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public AuthenticationResponse register(RegisterRequest request) throws UserAlreadyExistsException {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) throw new UserAlreadyExistsException();
        User user = User.builder()
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

    public AuthenticationResponse refreshToken(RefreshTokenRequest request) throws ExpiredJwtException, InvalidJWTException {
        if (tokenRepository.findByToken(request.getRefreshToken()).isPresent()) throw new InvalidJWTException();
        final String email = jwtService.extractEmail(request.getRefreshToken());

        if (Objects.nonNull(email)) {
            User user = this.userRepository.findByEmail(email).orElseThrow();
            if (jwtService.isTokenValid(request.getRefreshToken(), user)) {
                revokeAllUserTokens(user);
                String[] jwtTokens = this.generateTokens(user, request.getRefreshToken());
                return generateResponse(jwtTokens);
            } else {
                throw new InvalidJWTException("Emails do not match");
            }
        }
        return null;
    }

    private void revokeAllUserTokens(User user) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());
        if (validUserTokens.isEmpty()) return;
        validUserTokens.forEach(token -> {
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    private String[] generateTokens(User user, String refreshToken) {
        String jwtAccessToken = jwtService.generateToken(Map.of(), user);
        System.out.println(jwtAccessToken);
        String jwtRefreshToken = Objects.isNull(refreshToken) ? jwtService.generateRefreshToken(user) : refreshToken;
        Token token = Token.builder()
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
