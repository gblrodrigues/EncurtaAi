package com.gblrod.encurtaai.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.gblrod.encurtaai.config.RateLimitConfig.HEADER_LIMIT
import com.gblrod.encurtaai.config.RateLimitConfig.HEADER_REMAINING
import com.gblrod.encurtaai.config.RateLimitConfig.MAX_REQUESTS
import com.gblrod.encurtaai.dto.ErrorResponseDto
import com.gblrod.encurtaai.service.RateLimitService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.time.OffsetDateTime
import java.util.concurrent.TimeUnit

@Component
class RateLimitFilter(
    private val rateLimitService: RateLimitService,
    private val objectMapper: ObjectMapper
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val clientIp = request.getHeader("X-Forwarded-For")
            ?.split(",")
            ?.first()
            ?.trim()
            ?: request.remoteAddr
        val probe = rateLimitService.tryConsume(ip = clientIp)

        response.setHeader(HEADER_LIMIT, "$MAX_REQUESTS")
        response.setHeader(HEADER_REMAINING, "${probe.remainingTokens}")

        if (!probe.isConsumed) {
            val retryAfter = TimeUnit.NANOSECONDS.toSeconds(
                probe.nanosToWaitForRefill
            )

            response.setHeader(HttpHeaders.RETRY_AFTER, "$retryAfter")
            response.status = HttpStatus.TOO_MANY_REQUESTS.value()
            response.contentType = MediaType.APPLICATION_JSON_VALUE

            val errorResponse = ErrorResponseDto(
                timestamp = OffsetDateTime.now(),
                status = HttpStatus.TOO_MANY_REQUESTS.value(),
                error = HttpStatus.TOO_MANY_REQUESTS.reasonPhrase,
                message = "Rate limit exceeded. Try again in ${formatRetryAfter(retryAfter)}.",
                path = request.requestURI
            )

            response.writer.write(objectMapper.writeValueAsString(errorResponse))
            return
        }

        filterChain.doFilter(request, response)
    }

    private fun formatRetryAfter(retryAfter: Long): String {
        val minutes = retryAfter / 60
        val seconds = retryAfter % 60

        val minuteText = when (minutes) {
            1L -> "1 minute"
            else -> "$minutes minutes"
        }

        val secondText = when (seconds) {
            1L -> "1 second"
            else -> "$seconds seconds"
        }

        return when {
            minutes > 0 && seconds > 0 -> "$minuteText and $secondText"
            minutes > 0 -> minuteText
            else -> secondText
        }
    }
}