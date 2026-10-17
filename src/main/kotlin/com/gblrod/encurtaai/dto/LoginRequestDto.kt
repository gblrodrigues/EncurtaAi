package com.gblrod.encurtaai.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

@Schema(description = "Request payload for authenticating the administrator.")
data class LoginRequestDto(
    @field:Schema(
        description = "Administrator username.",
        example = "user"
    )
    @field:NotBlank(message = "Username must not be blank")
    val username: String,

    @field:Schema(
        description = "Administrator password.",
        example = "your-password",
        format = "password",
        accessMode = Schema.AccessMode.WRITE_ONLY
    )
    @field:NotBlank(message = "Password must not be blank")
    val password: String
)