package com.gblrod.encurtaai.security

import com.gblrod.encurtaai.config.SecurityProperties
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtService: JwtService,
    private val securityProperties: SecurityProperties
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION)

            if (authorizationHeader.isNullOrBlank() ||
                !authorizationHeader.startsWith(prefix = "Bearer ", ignoreCase = true)
            ) {
                filterChain.doFilter(request, response)
                return
            }

            val token = authorizationHeader
                .substring(7)
                .trim()

            if (token.isBlank()) {
                filterChain.doFilter(request, response)
                return
            }

            if (!jwtService.isTokenValid(token)) {
                SecurityContextHolder.clearContext()
                filterChain.doFilter(request, response)
                return
            }

            val username = jwtService.extractUsername(token)

            if (username != securityProperties.admin.username) {
                SecurityContextHolder.clearContext()
                filterChain.doFilter(request, response)
                return
            }

            val authentication = UsernamePasswordAuthenticationToken(
                username,
                null,
                listOf(SimpleGrantedAuthority("ROLE_ADMIN"))
            )

            SecurityContextHolder
                .getContext()
                .authentication = authentication

            filterChain.doFilter(request, response)

        } catch (_: Exception) {
            SecurityContextHolder.clearContext()
            filterChain.doFilter(request, response)

        } finally {
            SecurityContextHolder.clearContext()
        }
    }
}