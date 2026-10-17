package com.gblrod.encurtaai.controller

import com.gblrod.encurtaai.service.LinkService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.headers.Header
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@Tag(
    name = "Redirect",
    description = "Operations for redirecting users to the original URLs associated with shortened links."
)
@RestController
class RedirectController(
    private val service: LinkService
) {
    @Operation(
        summary = "Redirect to original URL",
        description = """
            Redirects to the original URL associated with the short code.
            This endpoint is public and does not require a JWT.
            Open the shortened URL directly in a browser to test the redirect.
        """
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "302",
                description = "Redirect successful. The Location header contains the original URL.",
                headers = [
                    Header(
                        name = "Location",
                        description = "Original destination URL",
                        schema = Schema(type = "string", format = "uri")
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
    fun redirect(
        @Parameter(
            description = "Short code identifying the shortened URL.",
            example = "my-github",
            required = true
        )
        @PathVariable code: String
    ): ResponseEntity<Void> {
        val link = service.registerAccess(code)

        return ResponseEntity
            .status(HttpStatus.FOUND)
            .location(URI.create(link.originalUrl))
            .build()
    }
}