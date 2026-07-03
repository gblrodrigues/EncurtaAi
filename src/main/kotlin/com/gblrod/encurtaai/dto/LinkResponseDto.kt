package com.gblrod.encurtaai.dto

data class LinkResponseDto(
    val id: Long,
    val originalUrl: String,
    val shortCode: String,
    val shortUrl: String
)