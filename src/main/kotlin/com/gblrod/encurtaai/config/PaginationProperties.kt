package com.gblrod.encurtaai.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.pagination")
data class PaginationProperties(
    val defaultSize: Int,
    val maxSize: Int,
    val defaultSort: String,
    val defaultDirection: String
)