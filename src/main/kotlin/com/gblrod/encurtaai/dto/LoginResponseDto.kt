package com.gblrod.encurtaai.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Response containing the JWT access token and its expiration.")
data class LoginResponseDto(
    @field:Schema(
        description = "JWT access token to use in the Authorization header.",
        example = "your-jwt-token"
    )
    val accessToken: String,

    @field:Schema(
        description = "Authentication scheme to use with the access token.",
        example = "Bearer"
    )
    val tokenType: String = "Bearer",

    @field:Schema(
        description = "Token validity duration in seconds.",
        example = "3600"
    )
    val expiresIn: Long
)