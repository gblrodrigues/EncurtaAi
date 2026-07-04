package com.gblrod.encurtaai.dto

data class PageResponseDto<T>(
    val data: List<T>,
    val pagination: PaginationDto
)