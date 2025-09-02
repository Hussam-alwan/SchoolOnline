package com.bootcamp.onlineschool.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Online School Backend API",
        version = "1.0.0",
        description = "REST API for managing an online school system with students, teachers, courses, classes, and registrations",
        contact = @Contact(
            name = "Bootcamp Team",
            email = "support@bootcamp.com",
            url = "https://bootcamp.com"
        ),
        license = @License(
            name = "MIT License",
            url = "https://opensource.org/licenses/MIT"
        )
    ),
    servers = {
        @Server(
            description = "Local Development Server",
            url = "http://localhost:8080"
        ),
        @Server(
            description = "Production Server",
            url = "https://api.onlineschool.com"
        )
    }
)
public class OpenApiConfig {
    // Configuration is handled through annotations
}