package com.gblrod.encurtaai.service

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RateLimitServiceTest {
    private lateinit var rateLimitService: RateLimitService

    @BeforeEach
    fun setUp() {
        rateLimitService = RateLimitService()
    }

    @Test
    fun shouldRejectRequestAfterRateLimitIsExhausted() {
        val tokens = 500

        val ip = "192.168.1.10"
        val ipRemaining = 0L

        repeat(times = tokens) {
            val result = rateLimitService.tryConsume(ip)
            assertTrue(result.isConsumed)
        }

        val result = rateLimitService.tryConsume(ip)

        assertFalse(result.isConsumed)
        assertEquals(ipRemaining, result.remainingTokens)
        assertTrue(result.nanosToWaitForRefill > 0)
    }

    @Test
    fun shouldMaintainIndependentLimitsForDifferentIPs() {
        val tokens = 500L

        val firstIp = "192.168.1.10"
        val firstIpConsume = 270
        val firstIpRemaining = tokens - firstIpConsume - 1

        val secondIp = "192.168.1.11"
        val secondIpConsume = 251
        val secondIpRemaining = tokens - secondIpConsume - 1

        repeat(times = firstIpConsume) {

            val result = rateLimitService.tryConsume(firstIp)
            assertTrue(result.isConsumed)
        }

        repeat(times = secondIpConsume) {
            val result = rateLimitService.tryConsume(secondIp)
            assertTrue(result.isConsumed)
        }

        val resultOne = rateLimitService.tryConsume(firstIp)
        val resultTwo = rateLimitService.tryConsume(secondIp)

        assertTrue(resultOne.isConsumed)
        assertEquals(firstIpRemaining, resultOne.remainingTokens)

        assertTrue(resultTwo.isConsumed)
        assertEquals(secondIpRemaining, resultTwo.remainingTokens)
    }
}