package com.gblrod.encurtaai.config

import com.gblrod.encurtaai.dto.ErrorResponseDto
import io.swagger.v3.core.converter.ModelConverters
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.media.Content
import io.swagger.v3.oas.models.media.MediaType
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.oas.models.responses.ApiResponse
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus

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
                    .addSchemas(
                        "ErrorResponseDto",
                        ModelConverters.getInstance()
                            .read(ErrorResponseDto::class.java)["ErrorResponseDto"]
                    )
                    .addSecuritySchemes(
                        "Bearer Authentication",
                        SecurityScheme()
                            .name("Authorization")
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")
                    )
                    .addResponses(
                        "BadRequest",
                        errorResponse(
                            description = "Request validation failed.",
                            status = HttpStatus.BAD_REQUEST.value(),
                            error = HttpStatus.BAD_REQUEST.reasonPhrase,
                            message = "One or more fields are invalid.",
                            path = "/example-endpoint"
                        )
                    )
                    .addResponses(
                        "Unauthorized",
                        errorResponse(
                            description = "Authentication is required or the JWT is invalid.",
                            status = HttpStatus.UNAUTHORIZED.value(),
                            error = HttpStatus.UNAUTHORIZED.reasonPhrase,
                            message = "Authentication is required to access this resource.",
                            path = "/example-endpoint"
                        )
                    )
                    .addResponses(
                        "NotFound",
                        errorResponse(
                            description = "The requested resource was not found.",
                            status = HttpStatus.NOT_FOUND.value(),
                            error = HttpStatus.NOT_FOUND.reasonPhrase,
                            message = "Link with code 'example-code' was not found.",
                            path = "/example-code"
                        )
                    )
                    .addResponses(
                        "Conflict",
                        errorResponse(
                            description = "The requested short code already exists.",
                            status = HttpStatus.CONFLICT.value(),
                            error = HttpStatus.CONFLICT.reasonPhrase,
                            message = "Short code 'example-code' already exists.",
                            path = "/example-endpoint"
                        )
                    )
                    .addResponses(
                        "TooManyRequests",
                        errorResponse(
                            description = "The per-IP rate limit has been exceeded. The response message includes the remaining wait time.",
                            status = HttpStatus.TOO_MANY_REQUESTS.value(),
                            error = HttpStatus.TOO_MANY_REQUESTS.reasonPhrase,
                            message = "Rate limit exceeded. Try again in 59 minutes and 30 seconds.",
                            path = "/example-endpoint"
                        )
                    )
                    .addResponses(
                        "InternalServerError",
                        errorResponse(
                            description = "An unexpected server error occurred.",
                            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            error = HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase,
                            message = "An unexpected server error occurred.",
                            path = "/example-endpoint"
                        )
                    )
            )
    }

    private fun errorResponse(
        description: String,
        status: Int,
        error: String,
        message: String,
        path: String
    ): ApiResponse =
        ApiResponse()
            .description(description)
            .content(
                Content().addMediaType(
                    "application/json",
                    MediaType().schema(Schema<Any>().`$ref`("#/components/schemas/ErrorResponseDto"))
                        .example(
                            mapOf(
                                "timestamp" to "2026-10-17T07:00:00-03:00",
                                "status" to status,
                                "error" to error,
                                "message" to message,
                                "path" to path
                            )
                        )
                )
            )
}