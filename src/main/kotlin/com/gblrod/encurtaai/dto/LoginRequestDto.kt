package com.gblrod.encurtaai.dto

import jakarta.validation.constraints.NotBlank

data class LoginRequestDto(
    @field:NotBlank(message = "Username must not be blank")
    val username: String,

    @field:NotBlank(message = "Password must not be blank")
    val password: String
)