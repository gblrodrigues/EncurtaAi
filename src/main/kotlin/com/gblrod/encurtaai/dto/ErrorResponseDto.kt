package com.gblrod.encurtaai.dto

import java.time.OffsetDateTime

data class ErrorResponseDto(
    val timestamp: OffsetDateTime,
    val status: Int,
    val error: String,
    val message: String,
    val path: String
)