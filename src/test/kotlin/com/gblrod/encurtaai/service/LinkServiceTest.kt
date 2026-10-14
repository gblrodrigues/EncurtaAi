package com.gblrod.encurtaai.service

import com.gblrod.encurtaai.config.AppProperties
import com.gblrod.encurtaai.config.PaginationProperties
import com.gblrod.encurtaai.dto.CreateLinkRequestDto
import com.gblrod.encurtaai.entity.Link
import com.gblrod.encurtaai.exception.ShortCodeAlreadyExistsException
import com.gblrod.encurtaai.repository.LinkRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import java.time.OffsetDateTime
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LinkServiceTest {
    private val linkRepository = Mockito.mock(LinkRepository::class.java)
    private val codeGenerator = Mockito.mock(CodeGenerator::class.java)
    private val appProperties = AppProperties(baseUrl = "http://localhost:8080")
    private val paginationProperties = PaginationProperties(
        defaultSize = 25,
        maxSize = 200,
        defaultSort = "createdAt",
        defaultDirection = "DESC"
    )

    private lateinit var linkService: LinkService

    @BeforeEach
    fun setUp() {
        linkService = LinkService(
            repository = linkRepository,
            codeGenerator = codeGenerator,
            appProperties = appProperties,
            paginationProperties = paginationProperties
        )
    }

    @Test
    fun shouldCreateLinkWithGeneratedShortCode() {
        val url = "https://youtube.com"
        val localHost = "http://localhost:8080"
        val generatedShortCode = "yt2026"
        val request = CreateLinkRequestDto(url = url, shortCode = null)

        Mockito.`when`(codeGenerator.generate()).thenReturn(generatedShortCode)
        Mockito.`when`(linkRepository.existsByShortCode(generatedShortCode)).thenReturn(false)
        Mockito.`when`(linkRepository.save(Mockito.any(Link::class.java)))
            .thenAnswer { invocation -> invocation.getArgument<Link>(0) }

        val result = linkService.create(request)

        assertEquals(url, result.originalUrl)
        assertEquals(generatedShortCode, result.shortCode)
        assertEquals("$localHost/$generatedShortCode", result.shortUrl)
    }

    @Test
    fun shouldCreateLinkWithCustomShortCode() {
        val url = "https://youtube.com"
        val localHost = "http://localhost:8080"
        val generatedShortCode = "yt2026"
        val request = CreateLinkRequestDto(url = url, shortCode = generatedShortCode)

        Mockito.`when`(codeGenerator.generate()).thenReturn(generatedShortCode)
        Mockito.`when`(linkRepository.existsByShortCode(generatedShortCode)).thenReturn(false)
        Mockito.`when`(linkRepository.save(Mockito.any(Link::class.java)))
            .thenAnswer { invocation -> invocation.getArgument<Link>(0) }

        val result = linkService.create(request)

        assertEquals(url, result.originalUrl)
        assertEquals(generatedShortCode, result.shortCode)
        assertEquals("$localHost/$generatedShortCode", result.shortUrl)
    }

    @Test
    fun shouldRejectDuplicateCustomShortCode() {
        val url = "https://youtube.com"
        val generatedShortCode = "yt2026"
        val requestOne = CreateLinkRequestDto(url = url, shortCode = generatedShortCode)

        Mockito.`when`(codeGenerator.generate()).thenReturn(generatedShortCode)
        Mockito.`when`(linkRepository.existsByShortCode(generatedShortCode)).thenReturn(true)

        assertThrows<ShortCodeAlreadyExistsException> { linkService.create(requestOne) }

        Mockito.verify(linkRepository).existsByShortCode(generatedShortCode)
        Mockito.verify(linkRepository, Mockito.never()).save(Mockito.any(Link::class.java))
        Mockito.verifyNoInteractions(codeGenerator)
    }

    @Test
    fun shouldRetryCodeGenerationWhenGeneratedCodeAlreadyExists() {
        val url = "https://youtube.com"
        val generatedShortCodeOne = "yt2026"
        val generatedShortCodeTwo = "yt2027"
        val requestOne = CreateLinkRequestDto(url = url, shortCode = null)

        Mockito.`when`(codeGenerator.generate()).thenReturn(generatedShortCodeOne, generatedShortCodeTwo)
        Mockito.`when`(linkRepository.existsByShortCode(generatedShortCodeOne)).thenReturn(true)
        Mockito.`when`(linkRepository.existsByShortCode(generatedShortCodeTwo)).thenReturn(false)
        Mockito.`when`(linkRepository.save(Mockito.any(Link::class.java)))
            .thenAnswer { invocation -> invocation.getArgument<Link>(0) }

        val result = linkService.create(requestOne)

        assertEquals(generatedShortCodeTwo, result.shortCode)

        Mockito.verify(codeGenerator, Mockito.times(2)).generate()
        Mockito.verify(linkRepository).save(Mockito.any(Link::class.java))
    }

    @Test
    fun shouldIncrementAccessCountWhenRegisteringAccess() {
        val url = "https://youtube.com"
        val generatedShortCodeOne = "yt2026"
        val previousLastAccessedAt = OffsetDateTime.now().minusDays(1)

        val link = Link(
            originalUrl = url,
            shortCode = generatedShortCodeOne,
            createdAt = OffsetDateTime.now()
        ).apply {
            accessCount = 3
            lastAccessedAt = previousLastAccessedAt
        }

        Mockito.`when`(linkRepository.findByShortCode(generatedShortCodeOne)).thenReturn(link)

        Mockito.`when`(linkRepository.save(Mockito.any(Link::class.java))
        ).thenAnswer { invocation -> invocation.getArgument<Link>(0) }

        val result = linkService.registerAccess(generatedShortCodeOne)

        assertEquals(4, result.accessCount)
        assertNotNull(result.lastAccessedAt)
        assertTrue(result.lastAccessedAt!!.isAfter(previousLastAccessedAt))

        Mockito.verify(linkRepository).save(link)
    }
}