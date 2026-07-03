package com.gblrod.encurtaai.service

import org.springframework.stereotype.Component
import java.security.SecureRandom

@Component
class CodeGenerator {
    private val secureRandom = SecureRandom()

    private companion object {
        private const val CODE_LENGTH = 6
        private const val CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
    }

    fun generate(): String {
        return buildString(capacity = CODE_LENGTH) {
            repeat(times = CODE_LENGTH) {
                append(CHARACTERS[secureRandom.nextInt(CHARACTERS.length)])
            }
        }
    }
}