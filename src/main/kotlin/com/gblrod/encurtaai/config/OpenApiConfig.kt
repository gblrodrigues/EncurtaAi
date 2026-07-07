package com.gblrod.encurtaai.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {
    @Bean
    fun customOpenApi(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("EncurtaAi API")
                    .description(
                        """
                        REST API for creating, managing and accessing shortened URLs.

                        Public features:
                        * Retrieve shortened URLs
                        * Search links by short code
                        * Redirect using generated short codes

                        Administrative features:
                        * Create shortened URLs
                        * Update existing URLs
                        * Delete shortened URLs

                        Authentication:
                        * JWT-based authentication for administrative operations
                        """.trimIndent()
                    )
                    .version("1.1.0")
                    .contact(
                        Contact()
                            .name("Gabriel Rodrigues")
                            .url("https://www.linkedin.com/in/gblrodrigues")
                    )
            )
            .components(
                Components()
                    .addSecuritySchemes(
                        "Bearer Authentication",
                        SecurityScheme()
                            .name("Authorization")
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")
                    )
            )
    }
}