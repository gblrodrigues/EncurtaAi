package com.gblrod.encurtaai.controller

import com.gblrod.encurtaai.service.LinkService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
class RedirectController(
    private val service: LinkService
) {
    @Operation(summary = "Redirect to original URL")
    @GetMapping("/{code}")
    fun redirect(@PathVariable code: String): ResponseEntity<Void> {
        val link = service.findEntityByCode(code)

        return ResponseEntity
            .status(HttpStatus.FOUND)
            .location(URI.create(link.originalUrl))
            .build()
    }
}