package com.project.backend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Melih Aydogdu",
                        email = "amelih6@gmail.com",
                        url = "https://melihaydogdu.bio"
                ),
                description = "OpenApi documentation for Spring Security",
                title = "OpenApi Specification",
                version = "1.0",
                license = @License(
                        name = "License name",
                        url = "https://some-licence.com"
                ),
                termsOfService = "Terms of Service"
        ),
        servers = {
                @Server(
                        description = "Local Env",
                        url = "http://localhost:8080/api/v1"
                ),
                @Server(
                        description = "Prod Env",
                        url = "http://prod.com"
                )
        },
        security = {
                @SecurityRequirement(name = "bearerAuth")
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
