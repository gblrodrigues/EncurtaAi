package com.gblrod.encurtaai.repository

import com.gblrod.encurtaai.entity.Link
import org.springframework.data.jpa.repository.JpaRepository

interface LinkRepository : JpaRepository<Link, Long> {
    fun existsByShortCode(shortCode: String): Boolean
    fun findByShortCode(shortCode: String): Link?
}