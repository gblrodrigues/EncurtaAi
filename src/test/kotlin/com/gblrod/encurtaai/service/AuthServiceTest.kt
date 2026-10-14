package com.gblrod.encurtaai.service

import com.gblrod.encurtaai.config.JwtProperties
import com.gblrod.encurtaai.config.SecurityProperties
import com.gblrod.encurtaai.dto.LoginRequestDto
import com.gblrod.encurtaai.security.JwtService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.crypto.password.PasswordEncoder
import kotlin.test.assertEquals

class AuthServiceTest {
    private val securityProperties = Mockito.mock(SecurityProperties::class.java, Mockito.RETURNS_DEEP_STUBS)
    private val passwordEncoder = Mockito.mock(PasswordEncoder::class.java)
    private val jwtService = Mockito.mock(JwtService::class.java)
    private val jwtProperties = Mockito.mock(JwtProperties::class.java)
    private lateinit var authService: AuthService

    @BeforeEach
    fun setUp() {
        authService = AuthService(
            securityProperties = securityProperties,
            passwordEncoder = passwordEncoder,
            jwtService = jwtService,
            jwtProperties = jwtProperties
        )
    }

    @Test
    fun shouldAuthenticateWithValidCredentials() {
        Mockito.`when`(securityProperties.admin.username).thenReturn("admin")
        Mockito.`when`(securityProperties.admin.passwordHash).thenReturn("encoded-password")
        Mockito.`when`(passwordEncoder.matches("my-password", "encoded-password")).thenReturn(true)
        Mockito.`when`(jwtService.generateToken(username = "admin")).thenReturn("jwt-token")
        Mockito.`when`(jwtProperties.expiration).thenReturn(3_600_000L)

        val request = LoginRequestDto(username = "admin", password = "my-password")
        val result = authService.authenticate(request)

        assertEquals("jwt-token", result.accessToken)
        assertEquals(3600, result.expiresIn)

        Mockito.verify(jwtService).generateToken(username = "admin")
    }

    @Test
    fun shouldRejectInvalidUsername() {
        Mockito.`when`(securityProperties.admin.username).thenReturn("admin")

        val request = LoginRequestDto(username = "usernameIncorrect", password = "my-password")
        assertThrows<BadCredentialsException> { authService.authenticate(request = request) }

        Mockito.verifyNoInteractions(passwordEncoder, jwtService, jwtProperties)
    }

    @Test
    fun shouldRejectInvalidPassword() {
        Mockito.`when`(securityProperties.admin.username).thenReturn("admin")
        Mockito.`when`(securityProperties.admin.passwordHash).thenReturn("encoded-password")
        Mockito.`when`(passwordEncoder.matches("my-incorrect-password", "encoded-password")).thenReturn(false)
        Mockito.`when`(jwtService.generateToken(username = "admin")).thenReturn("jwt-token")
        Mockito.`when`(jwtProperties.expiration).thenReturn(3_600_000L)

        val request = LoginRequestDto(username = "admin", password = "my-incorrect-password")
        assertThrows<BadCredentialsException> { authService.authenticate(request = request) }

        Mockito.verify(passwordEncoder).matches("my-incorrect-password", "encoded-password")
        Mockito.verifyNoInteractions(jwtService, jwtProperties)
    }
}