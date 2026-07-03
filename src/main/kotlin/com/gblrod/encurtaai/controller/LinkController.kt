package com.gblrod.encurtaai.controller

import com.gblrod.encurtaai.dto.CreateLinkRequestDto
import com.gblrod.encurtaai.dto.LinkResponseDto
import com.gblrod.encurtaai.dto.UpdateLinkRequestDto
import com.gblrod.encurtaai.service.LinkService
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/links")
class LinkController(
    private val service: LinkService
) {
    @Operation(summary = "Create a shortened URL")
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    fun create(@RequestBody @Valid request: CreateLinkRequestDto): LinkResponseDto {
        return service.create(request)
    }

    @Operation(summary = "List all links")
    @GetMapping
    fun findAll(): List<LinkResponseDto> {
        return service.findAll()
    }

    @Operation(summary = "Get link by short code")
    @GetMapping("/{code}")
    fun findByCode(@PathVariable code: String): LinkResponseDto {
        return service.findByCode(code)
    }

    @Operation(summary = "Update a link")
    @PutMapping("/{code}")
    fun update(@PathVariable code: String, @RequestBody @Valid request: UpdateLinkRequestDto): LinkResponseDto {
        return service.update(code, request)
    }

    @Operation(summary = "Delete a link")
    @DeleteMapping("/{code}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable code: String) {
        service.delete(code)
    }
}