package com.gblrod.encurtaai

import com.gblrod.encurtaai.config.AppProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
@EnableConfigurationProperties(AppProperties::class)
@SpringBootApplication
class EncurtaaiApplication

fun main(args: Array<String>) {
    runApplication<EncurtaaiApplication>(*args)
}