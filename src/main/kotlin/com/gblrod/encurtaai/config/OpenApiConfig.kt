package com.gblrod.encurtaai.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
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
                        
                        Features:
                        * Create short URLs
                        * Retrieve all shortened URLs
                        * Search by short code
                        * Update existing URLs
                        * Delete shortened URLs
                        * Redirect using the generated short code
                        """.trimIndent()
                    )
                    .version("1.0.0")
                    .contact(
                        Contact()
                            .name("Gabriel Rodrigues")
                            .url("https://www.linkedin.com/in/gblrodrigues")
                    )
            )
    }
}