package com.gblrod.encurtaai.config

import io.github.bucket4j.Bandwidth
import java.time.Duration

object RateLimitConfig {
    const val MAX_REQUESTS = 500L
    const val HEADER_LIMIT = "X-RateLimit-Limit"
    const val HEADER_REMAINING = "X-RateLimit-Remaining"
    val REFILL_DURATION: Duration = Duration.ofHours(1)

    val bandwidth: Bandwidth = Bandwidth.builder()
        .capacity(MAX_REQUESTS)
        .refillIntervally(MAX_REQUESTS, REFILL_DURATION)
        .build()
}