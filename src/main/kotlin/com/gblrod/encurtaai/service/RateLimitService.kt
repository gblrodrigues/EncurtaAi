package com.gblrod.encurtaai.service

import com.gblrod.encurtaai.config.RateLimitConfig
import io.github.bucket4j.Bucket
import io.github.bucket4j.ConsumptionProbe
import org.springframework.stereotype.Service

@Service
class RateLimitService {
    private val bucket: Bucket = Bucket.builder()
        .addLimit(RateLimitConfig.bandwidth)
        .build()

    fun tryConsume(): ConsumptionProbe {
        return bucket.tryConsumeAndReturnRemaining(1)
    }
}