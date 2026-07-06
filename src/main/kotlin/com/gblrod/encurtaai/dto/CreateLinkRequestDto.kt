package com.gblrod.encurtaai.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import org.hibernate.validator.constraints.URL

data class CreateLinkRequestDto(
    @field:Schema(
        description = "Original URL to be shortened.",
        example = "https://www.linkedin.com/in/gblrodrigues/"
    )
    @field:NotBlank(message = "URL must not be blank")
    @field:URL(message = "Invalid URL")
    val url: String,
    @field:Schema(
        description = "Custom short code (3-20 characters).",
        example = "my-linkedin"
    )
    @field:Pattern(
        regexp = "^[a-zA-Z0-9_-]+$",
        message = "Short code contains invalid characters."
    )
    @field:Size(min = 3, max = 20, message = "Short code must be between 3 and 20 characters.")
    val shortCode: String?
)