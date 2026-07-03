package com.gblrod.encurtaai.extension

import com.gblrod.encurtaai.dto.LinkResponseDto
import com.gblrod.encurtaai.entity.Link

fun Link.toResponse(baseUrl: String): LinkResponseDto {
    return LinkResponseDto(
        id = id,
        originalUrl = originalUrl,
        shortCode = shortCode,
        shortUrl = "$baseUrl/$shortCode"
    )
}