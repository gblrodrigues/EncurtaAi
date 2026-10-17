package com.gblrod.encurtaai.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Pagination metadata for a list response.")
data class PaginationDto(
    @field:Schema(description = "Zero-based index of the current page.", example = "0")
    val page: Int,

    @field:Schema(description = "Number of items requested per page.", example = "25")
    val size: Int,

    @field:Schema(description = "Total number of items matching the query across all pages.", example = "125")
    val totalItems: Long,

    @field:Schema(description = "Total number of pages available for the requested page size.", example = "20")
    val totalPages: Int
)