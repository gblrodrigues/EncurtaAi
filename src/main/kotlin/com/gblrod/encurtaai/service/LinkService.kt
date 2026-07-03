package com.gblrod.encurtaai.service

import com.gblrod.encurtaai.config.AppProperties
import com.gblrod.encurtaai.dto.CreateLinkRequestDto
import com.gblrod.encurtaai.dto.LinkResponseDto
import com.gblrod.encurtaai.dto.UpdateLinkRequestDto
import com.gblrod.encurtaai.entity.Link
import com.gblrod.encurtaai.exception.LinkNotFoundException
import com.gblrod.encurtaai.exception.ShortCodeAlreadyExistsException
import com.gblrod.encurtaai.extension.toResponse
import com.gblrod.encurtaai.repository.LinkRepository
import org.springframework.stereotype.Service
import java.time.OffsetDateTime

@Service
class LinkService(
    private val repository: LinkRepository,
    private val codeGenerator: CodeGenerator,
    private val appProperties: AppProperties
) {
    fun create(request: CreateLinkRequestDto): LinkResponseDto {
        val code = generateUniqueCode()

        val link = Link(
            originalUrl = request.url,
            shortCode = code,
            createdAt = OffsetDateTime.now()
        )

        val saved = repository.save(link)

        return saved.toResponse(appProperties.baseUrl)
    }

    fun findEntityByCode(code: String): Link {
        return repository.findByShortCode(code) ?: throw LinkNotFoundException(code = code)
    }

    fun findByCode(code: String): LinkResponseDto {
        return findEntityByCode(code = code).toResponse(appProperties.baseUrl)
    }

    fun findAll(): List<LinkResponseDto> {
        return repository.findAll().map { it.toResponse(appProperties.baseUrl) }
    }

    fun update(code: String, request: UpdateLinkRequestDto): LinkResponseDto {
        val link = findEntityByCode(code)

        if (request.shortCode != link.shortCode && repository.existsByShortCode(request.shortCode)) {
            throw ShortCodeAlreadyExistsException(code = request.shortCode)
        }

        link.originalUrl = request.originalUrl
        link.shortCode = request.shortCode

        val saved = repository.save(link)
        return saved.toResponse(appProperties.baseUrl)
    }

    fun delete(code: String) {
        val link = findEntityByCode(code)

        repository.delete(link)
    }

    private fun generateUniqueCode(): String {
        var code: String

        do {
            code = codeGenerator.generate()
        } while (repository.existsByShortCode(code))

        return code
    }
}