package com.gblrod.encurtaai.controller

import com.gblrod.encurtaai.dto.LoginRequestDto
import com.gblrod.encurtaai.dto.LoginResponseDto
import com.gblrod.encurtaai.service.AuthService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("/login")
    fun login(@RequestBody @Valid request: LoginRequestDto): LoginResponseDto {
        return authService.authenticate(request)
    }
}