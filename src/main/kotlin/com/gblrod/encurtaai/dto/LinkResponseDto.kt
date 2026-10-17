package com.gblrod.encurtaai.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.time.OffsetDateTime

@Schema(description = "Response containing the details of a shortened URL.")
data class LinkResponseDto(
    @field:Schema(
        description = "Unique identifier of the link.",
        example = "1"
    )
    val id: Long,

    @field:Schema(
        description = "Original destination URL.",
        example = "https://www.linkedin.com/in/gblrodrigues/"
    )
    val originalUrl: String,

    @field:Schema(
        description = "Short code identifying the link.",
        example = "my-linkedin"
    )
    val shortCode: String,

    @field:Schema(
        description = "Complete shortened URL that can be shared or opened.",
        example = "https://encurtaai-5q3b.onrender.com/my-linkedin"
    )
    val shortUrl: String,

    @field:Schema(
        description = "Total number of times the shortened URL has been accessed.",
        example = "26"
    )
    val accessCount: Long,

    @field:Schema(
        description = "Timestamp of the most recent access. Null if the link has never been accessed.",
        example = "2026-10-17T18:57:43.605374Z"
    )
    val lastAccessedAt: OffsetDateTime?
)