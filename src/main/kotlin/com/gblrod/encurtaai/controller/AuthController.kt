package com.gblrod.encurtaai.controller

import com.gblrod.encurtaai.dto.LoginRequestDto
import com.gblrod.encurtaai.dto.LoginResponseDto
import com.gblrod.encurtaai.service.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(
    name = "Auth",
    description = "Operations for user authentication and authorization."
)
@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authService: AuthService
) {
    @Operation(
        summary = "Authenticate administrator and obtain JWT",
        description = """
            Authenticates the administrator using username and password.
            
            On successful authentication, returns an access token, its token type,
            and its expiration time.
            
            Use the returned access token as a Bearer token when calling
            endpoints that require administrator authentication.
        """,
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Authentication successful. JWT access token returned.",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = LoginResponseDto::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "400",
                ref = "#/components/responses/BadRequest"
            ),
            ApiResponse(
                responseCode = "401",
                ref = "#/components/responses/Unauthorized"
            ),
            ApiResponse(
                responseCode = "429",
                ref = "#/components/responses/TooManyRequests"
            ),
            ApiResponse(
                responseCode = "500",
                ref = "#/components/responses/InternalServerError"
            )
        ]
    )
    @PostMapping("/login")
    fun login(@RequestBody @Valid request: LoginRequestDto): LoginResponseDto {
        return authService.authenticate(request)
    }
}