package com.gblrod.encurtaai

import com.gblrod.encurtaai.config.AppProperties
import com.gblrod.encurtaai.config.JwtProperties
import com.gblrod.encurtaai.config.PaginationProperties
import com.gblrod.encurtaai.config.SecurityProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@EnableConfigurationProperties(
    AppProperties::class,
    PaginationProperties::class,
    SecurityProperties::class,
    JwtProperties::class
)
@SpringBootApplication
class EncurtaaiApplication

fun main(args: Array<String>) {
    runApplication<EncurtaaiApplication>(*args)
}