package com.gblrod.encurtaai.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import org.hibernate.validator.constraints.URL

@Schema(
    description = "Request payload for updating an existing shortened URL."
)
data class UpdateLinkRequestDto(
    @field:Schema(
        description = "New destination URL for the shortened link.",
        example = "https://www.linkedin.com/in/gblrodrigues/"
    )

    @field:URL(message = "Invalid URL")
    @field:NotBlank(message = "URL must not be blank")
    val originalUrl: String,

    @field:Schema(
        description = """
            New short code for the link. Must contain 3-20 characters
            and use only letters, numbers, underscores or hyphens.
            The code must be unique if it differs from the current code.
        """,
        example = "linkedin"
    )
    @field:Pattern(
        regexp = "^[a-zA-Z0-9_-]+$",
        message = "Short code contains invalid characters."
    )
    @field:Size(min = 3, max = 20, message = "Short code must be between 3 and 20 characters.")
    @field:NotBlank(message = "Short code must not be blank")
    val shortCode: String
)