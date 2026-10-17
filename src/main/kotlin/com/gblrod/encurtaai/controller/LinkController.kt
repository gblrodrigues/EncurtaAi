package com.gblrod.encurtaai.controller

import com.gblrod.encurtaai.dto.CreateLinkRequestDto
import com.gblrod.encurtaai.dto.LinkResponseDto
import com.gblrod.encurtaai.dto.PageResponseDto
import com.gblrod.encurtaai.dto.UpdateLinkRequestDto
import com.gblrod.encurtaai.service.LinkService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@Tag(
    name = "Links",
    description = "Operations for creating, retrieving, updating and deleting shortened URLs."
)
@RestController
@RequestMapping("/api/v1/links")
class LinkController(
    private val service: LinkService
) {
    @Operation(
        summary = "Create a shortened URL",
        description = """
            Creates a shortened URL.
            
            Provide a custom short code or omit it to generate one automatically.
            The custom code must be unique.
            
            Requires administrator authentication. 
        """,
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Shortened URL created successfully.",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = LinkResponseDto::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "400",
                ref = "#/components/responses/BadRequest"
            ),
            ApiResponse(
                responseCode = "401",
                ref = "#/components/responses/Unauthorized"
            ),
            ApiResponse(
                responseCode = "409",
                ref = "#/components/responses/Conflict"
            ),
            ApiResponse(
                responseCode = "429",
                ref = "#/components/responses/TooManyRequests"
            ),
            ApiResponse(
                responseCode = "500",
                ref = "#/components/responses/InternalServerError"
            )
        ]
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    fun create(@RequestBody @Valid request: CreateLinkRequestDto): LinkResponseDto {
        return service.create(request)
    }

    @Operation(
        summary = "List shortened URLs",
        description = """
            Returns a paginated list of shortened URLs.
            
            Use page to select the page index, size to set the number of
            results per page, and sort to specify the sort field and direction.
            
            This endpoint is publicly accessible.
        """
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Paginated list of shortened URLs.",
                useReturnTypeSchema = true
            ),
            ApiResponse(
                responseCode = "400",
                ref = "#/components/responses/BadRequest"
            ),
            ApiResponse(
                responseCode = "429",
                ref = "#/components/responses/TooManyRequests"
            ),
            ApiResponse(
                responseCode = "500",
                ref = "#/components/responses/InternalServerError"
            )
        ]
    )
    @GetMapping
    fun findAll(
        @Parameter(
            description = "Zero-based index of the page to retrieve.",
            example = "0"
        )
        @RequestParam(required = false)
        page: Int?,

        @Parameter(
            description = "Number of items requested per page.",
            example = "25"
        )
        @RequestParam(required = false)
        size: Int?,

        @Parameter(
            description = """
                Sorting criteria using the format property,direction.

                Supported properties: id, originalUrl, shortCode,
                createdAt, accessCount, lastAccessedAt.

                Direction must be ASC or DESC.
                If direction is omitted, the configured default direction is used.
                If sort is omitted, the configured default property and direction are used.
            """,
            example = "createdAt,DESC"
        )
        @RequestParam(required = false)
        sort: String?
    ): PageResponseDto<LinkResponseDto> {
        return service.findAll(page, size, sort)
    }

    @Operation(
        summary = "Get link details by short code",
        description = """
            Retrieves the details of a shortened URL using its short code.

            The response includes the original URL, short code, shortened URL,
            access count, and last access timestamp.

            This endpoint returns link information; it does not redirect
            the client to the original URL.
            
            This endpoint is publicly accessible.
        """
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Shortened URL retrieved successfully.",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = LinkResponseDto::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "404",
                ref = "#/components/responses/NotFound"
            ),
            ApiResponse(
                responseCode = "429",
                ref = "#/components/responses/TooManyRequests"
            ),
            ApiResponse(
                responseCode = "500",
                ref = "#/components/responses/InternalServerError"
            )
        ]
    )
    @GetMapping("/{code}")
    fun findByCode(
        @Parameter(
            description = "Short code identifying the link to retrieve.",
            example = "my-linkedin",
            required = true
        )
        @PathVariable code: String
    ): LinkResponseDto {
        return service.findByCode(code)
    }

    @Operation(
        summary = "Update a shortened URL",
        description = """
            Updates an existing shortened URL using its current short code.

            The request specifies the updated original URL and short code.
            The new short code must be unique if it differs from the current one.

            Requires administrator authentication.
        """
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Shortened URL updated successfully.",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = LinkResponseDto::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "400",
                ref = "#/components/responses/BadRequest"
            ),
            ApiResponse(
                responseCode = "401",
                ref = "#/components/responses/Unauthorized"
            ),
            ApiResponse(
                responseCode = "404",
                ref = "#/components/responses/NotFound"
            ),
            ApiResponse(
                responseCode = "409",
                ref = "#/components/responses/Conflict"
            ),
            ApiResponse(
                responseCode = "429",
                ref = "#/components/responses/TooManyRequests"
            ),
            ApiResponse(
                responseCode = "500",
                ref = "#/components/responses/InternalServerError"
            )
        ]
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("/{code}")
    fun update(
        @Parameter(
            description = "Current short code identifying the link to update.",
            example = "my-linkedin",
            required = true
        )
        @PathVariable code: String,
        @RequestBody @Valid request: UpdateLinkRequestDto
    ): LinkResponseDto {
        return service.update(code, request)
    }

    @Operation(
        summary = "Delete a shortened URL",
        description = """
            Deletes an existing shortened URL identified by its current short code.

            Returns HTTP 204 No Content when the deletion succeeds.

            Requires administrator authentication.
        """
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "204",
                description = "Shortened URL deleted successfully. No content is returned."
            ),
            ApiResponse(
                responseCode = "401",
                ref = "#/components/responses/Unauthorized"
            ),
            ApiResponse(
                responseCode = "404",
                ref = "#/components/responses/NotFound"
            ),
            ApiResponse(
                responseCode = "429",
                ref = "#/components/responses/TooManyRequests"
            ),
            ApiResponse(
                responseCode = "500",
                ref = "#/components/responses/InternalServerError"
            )
        ]
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/{code}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(
        @Parameter(
            description = "Short code identifying the link to delete.",
            example = "my-linkedin",
            required = true
        )
        @PathVariable code: String
    ) {
        service.delete(code)
    }
}