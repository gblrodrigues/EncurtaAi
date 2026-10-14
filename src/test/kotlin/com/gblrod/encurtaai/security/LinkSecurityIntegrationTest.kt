package com.gblrod.encurtaai.security

import com.gblrod.encurtaai.config.SecurityProperties
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class LinkSecurityIntegrationTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var jwtService: JwtService

    @Autowired
    private lateinit var securityProperties: SecurityProperties

    @Test
    fun shouldReturnUnauthorizedWhenCreatingLinkWithoutToken() {
        mockMvc.perform(
            post("/api/v1/links")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                        "url": "https://example.com",
                        "shortCode": "security-test"
                    }
                    """.trimIndent()
                )
        )
            .andExpect(status().isUnauthorized)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.status").value(401))
            .andExpect(jsonPath("$.error").value("Unauthorized"))
            .andExpect(jsonPath("$.message").value("Authentication is required to access this resource."))
            .andExpect(jsonPath("$.path").value("/api/v1/links"))
    }

    @Test
    fun shouldAllowCreatingLinkWithValidToken() {
        val token = jwtService.generateToken(username = securityProperties.admin.username)

        mockMvc.perform(
            post("/api/v1/links")
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                        "url": "https://example.com",
                        "shortCode": null
                    }
                    """.trimIndent()
                )
        )
            .andExpect(status().is2xxSuccessful)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.originalUrl").value("https://example.com"))
            .andExpect(jsonPath("$.shortCode").exists())
            .andExpect(jsonPath("$.shortUrl").exists())
    }
}