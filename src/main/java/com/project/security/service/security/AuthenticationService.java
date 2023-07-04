package com.project.security.service;

import com.project.security.dto.request.AuthenticationRequest;
import com.project.security.dto.request.RegisterRequest;
import com.project.security.dto.response.AuthenticationResponse;
import com.project.security.model.token.Token;
import com.project.security.model.token.TokenRepository;
import com.project.security.model.token.TokenType;
import com.project.security.model.user.Role;
import com.project.security.model.user.User;
import com.project.security.model.user.UserRepository;
import com.project.security.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user);
        String jwtToken = this.generateToken(user);
        return generateResponse(jwtToken);
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        revokeAllUserTokens((User) authenticate.getPrincipal());
        String jwtToken = this.generateToken((User) authenticate.getPrincipal());
        return generateResponse(jwtToken);
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

    private String generateToken(User user) {
        var jwtToken = jwtService.generateToken(Map.of(), user);
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .build();
        tokenRepository.save(token);
        return jwtToken;
    }

    private AuthenticationResponse generateResponse(String jwtToken) {
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();
    }

}
