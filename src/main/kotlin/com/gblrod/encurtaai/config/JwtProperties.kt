package com.gblrod.encurtaai.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "security.jwt")
data class JwtProperties(
    val secret: String,
    val expiration: Long
)