package com.gblrod.encurtaai.config

import com.gblrod.encurtaai.security.JwtAuthenticationFilter
import com.gblrod.encurtaai.security.RateLimitFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val rateLimitFilter: RateLimitFilter
) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authorizeHttpRequests {
                it.requestMatchers(
                    "/swagger/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/api-docs/**",
                    "/v3/api-docs/**"
                ).permitAll()

                it.requestMatchers(
                    HttpMethod.POST,
                    "/api/v1/auth/login"
                ).permitAll()

                it.requestMatchers(
                    HttpMethod.GET,
                    "/api/v1/links",
                    "/api/v1/links/*"
                ).permitAll()

                it.requestMatchers(HttpMethod.GET, "/*").permitAll()

                it.anyRequest().authenticated()
            }

        http.addFilterBefore(
            rateLimitFilter,
            UsernamePasswordAuthenticationFilter::class.java
        )

        http.addFilterBefore(
            jwtAuthenticationFilter,
            UsernamePasswordAuthenticationFilter::class.java
        )

        return http.build()
    }
}