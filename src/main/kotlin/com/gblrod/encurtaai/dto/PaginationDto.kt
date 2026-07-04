package com.gblrod.encurtaai.dto

data class PaginationDto(
    val page: Int,
    val size: Int,
    val totalItems: Long,
    val totalPages: Int
)