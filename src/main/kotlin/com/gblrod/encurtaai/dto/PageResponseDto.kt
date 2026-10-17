package com.gblrod.encurtaai.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Paginated response containing the requested data and pagination metadata.")
data class PageResponseDto<T>(
    @field:Schema(description = "Items returned for the requested page.")
    val data: List<T>,

    @field:Schema(description = "Metadata describing the current page and the total available results.")
    val pagination: PaginationDto
)