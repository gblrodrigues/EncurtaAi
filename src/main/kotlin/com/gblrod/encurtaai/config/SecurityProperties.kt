package com.gblrod.encurtaai.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "security")
data class SecurityProperties(
    val admin: Admin,
    val jwt: Jwt
) {
    data class Admin(
        val username: String,
        val passwordHash: String
    )

    data class Jwt(
        val secret: String,
        val expiration: Long
    )
}