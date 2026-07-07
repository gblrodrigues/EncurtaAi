package com.gblrod.encurtaai.dto

data class LoginResponseDto(
    val accessToken: String,
    val tokenType: String = "Bearer",
    val expiresIn: Long
)