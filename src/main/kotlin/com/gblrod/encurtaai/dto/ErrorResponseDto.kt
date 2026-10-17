package com.gblrod.encurtaai.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.time.OffsetDateTime

@Schema(description = "Standardized error response returned when an API request fails.")
data class ErrorResponseDto(
    @field:Schema(description = "Date and time when the error response was generated.")
    val timestamp: OffsetDateTime,

    @field:Schema(description = "HTTP status code returned by the API.")
    val status: Int,

    @field:Schema(description = "HTTP status reason phrase.")
    val error: String,

    @field:Schema(description = "Description of the error that occurred.")
    val message: String,

    @field:Schema(description = "Request URI that produced the error.")
    val path: String
)