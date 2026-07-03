package com.gblrod.encurtaai.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.URL

data class CreateLinkRequestDto(
    @field:Schema(
        description = "Original URL to be shortened.",
        example = "https://www.linkedin.com/in/gblrodrigues/"
    )
    @field:NotBlank(message = "URL must not be blank")
    @field:URL(message = "Invalid URL")
    val url: String
)