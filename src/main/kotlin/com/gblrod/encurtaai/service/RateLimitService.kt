package com.gblrod.encurtaai.service

import com.gblrod.encurtaai.config.RateLimitConfig
import io.github.bucket4j.Bucket
import io.github.bucket4j.ConsumptionProbe
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

@Service
class RateLimitService {
    private val buckets = ConcurrentHashMap<String, Bucket>()

    fun tryConsume(ip: String): ConsumptionProbe {
        val bucket = buckets.computeIfAbsent(ip) {
            Bucket.builder()
                .addLimit(RateLimitConfig.bandwidth)
                .build()
        }
        return bucket.tryConsumeAndReturnRemaining(1)
    }
}