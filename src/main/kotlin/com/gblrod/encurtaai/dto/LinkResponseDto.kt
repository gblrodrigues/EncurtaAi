package com.gblrod.encurtaai.dto

import java.time.OffsetDateTime

data class LinkResponseDto(
    val id: Long,
    val originalUrl: String,
    val shortCode: String,
    val shortUrl: String,
    val accessCount: Long,
    val lastAccessedAt: OffsetDateTime?
)