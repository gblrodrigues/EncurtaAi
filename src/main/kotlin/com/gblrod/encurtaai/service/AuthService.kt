package com.gblrod.encurtaai.service

import com.gblrod.encurtaai.config.JwtProperties
import com.gblrod.encurtaai.config.SecurityProperties
import com.gblrod.encurtaai.dto.LoginRequestDto
import com.gblrod.encurtaai.dto.LoginResponseDto
import com.gblrod.encurtaai.security.JwtService
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val securityProperties: SecurityProperties,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val jwtProperties: JwtProperties
) {
    fun authenticate(request: LoginRequestDto): LoginResponseDto {
        if (request.username != securityProperties.admin.username) {
            throw BadCredentialsException("Invalid credentials")
        }

        val passwordMatches = passwordEncoder.matches(
            request.password,
            securityProperties.admin.passwordHash
        )

        if (!passwordMatches) {
            throw BadCredentialsException("Invalid credentials")
        }

        return LoginResponseDto(
            accessToken = jwtService.generateToken(request.username),
            expiresIn = jwtProperties.expiration / 1000
        )
    }
}