package com.project.backend.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {

    @NotBlank(message = "Email required")
    @Schema(example = "amelih6@gmail.com")
    private String email;

    @NotBlank(message = "Password required")
    @Schema(example = "pass")
    private String password;
}
