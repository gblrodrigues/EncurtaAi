package com.gblrod.encurtaai.service

import com.gblrod.encurtaai.config.AppProperties
import com.gblrod.encurtaai.config.PaginationProperties
import com.gblrod.encurtaai.dto.*
import com.gblrod.encurtaai.entity.Link
import com.gblrod.encurtaai.exception.LinkNotFoundException
import com.gblrod.encurtaai.exception.ShortCodeAlreadyExistsException
import com.gblrod.encurtaai.mapper.toResponse
import com.gblrod.encurtaai.repository.LinkRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.OffsetDateTime

@Service
class LinkService(
    private val repository: LinkRepository,
    private val codeGenerator: CodeGenerator,
    private val appProperties: AppProperties,
    private val paginationProperties: PaginationProperties
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

    fun findAll(page: Int?, size: Int?, sort: String?): PageResponseDto<LinkResponseDto> {
        val currentPage = page ?: 0
        val currentSize = (size ?: paginationProperties.defaultSize).coerceIn(1, paginationProperties.maxSize)
        val currentSort = sort ?: "${paginationProperties.defaultSort},${paginationProperties.defaultDirection}"

        val result = repository.findAll(createPageable(currentPage, currentSize, currentSort))

        return PageResponseDto(
            data = result.content.map { it.toResponse(appProperties.baseUrl) },
            pagination = PaginationDto(
                page = result.number,
                size = result.size,
                totalItems = result.totalElements,
                totalPages = result.totalPages
            )
        )
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

    @Transactional
    fun registerAccess(code: String): Link {
        val link = findEntityByCode(code)

        link.accessCount++
        link.lastAccessedAt = OffsetDateTime.now()

        return repository.save(link)
    }

    private fun generateUniqueCode(): String {
        var code: String

        do {
            code = codeGenerator.generate()
        } while (repository.existsByShortCode(code))

        return code
    }

    private fun createPageable(page: Int, size: Int, sort: String): Pageable {
        val parts = sort.split(",")
        val field = parts.first()
        val direction = parts.getOrNull(index = 1) ?: paginationProperties.defaultDirection

        return PageRequest.of(
            page,
            size.coerceIn(1, paginationProperties.maxSize),
            Sort.by(
                Sort.Direction.fromString(direction),
                field
            )
        )
    }
}