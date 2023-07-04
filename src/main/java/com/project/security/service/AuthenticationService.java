package com.project.security.service;

import com.project.security.dto.request.AuthenticationRequest;
import com.project.security.dto.request.RegisterRequest;
import com.project.security.dto.response.AuthenticationResponse;
import com.project.security.model.Role;
import com.project.security.model.User;
import com.project.security.repository.UserRepository;
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

        return this.generateResponse(user);
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        return this.generateResponse((User) authenticate.getPrincipal());
    }

    private AuthenticationResponse generateResponse (User user) {
        var jwtToken = jwtService.generateToken(Map.of(), user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

}
