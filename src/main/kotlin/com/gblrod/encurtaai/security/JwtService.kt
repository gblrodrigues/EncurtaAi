package com.gblrod.encurtaai.security

import com.gblrod.encurtaai.config.JwtProperties
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwt
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.util.Date

@Service
class JwtService(
    private val jwtProperties: JwtProperties
) {
    private val key = Keys.hmacShaKeyFor(
        jwtProperties.secret.toByteArray(StandardCharsets.UTF_8)
    )

    fun generateToken(username: String): String {
        val now = Date()
        val expiration = Date(now.time + jwtProperties.expiration)

        return Jwts.builder()
            .subject(username)
            .issuedAt(now)
            .expiration(expiration)
            .signWith(key)
            .compact()
    }

    fun extractUsername(token: String): String {
        return parseToken(token)
            .payload
            .subject
    }

    fun isTokenValid(token: String): Boolean {
        return try {
            parseToken(token)
            true
        } catch (_: Exception) {
            false
        }
    }

    private fun parseToken(token: String): Jwt<*, Claims> {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
    }
}