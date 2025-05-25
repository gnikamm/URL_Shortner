package com.urlshortnerproject.repository

import com.urlshortnerproject.model.UrlMapping
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UrlMappingRepository : JpaRepository<UrlMapping, Int> {
    fun findByShortCode(shortCode: String): UrlMapping?
}