package com.urlshortnerproject.repository

import com.urlshortnerproject.model.ShortUrl
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UrlMappingRepository : JpaRepository<ShortUrl, Int> {

    fun findByShortCode(shortCode: String): ShortUrl?

    fun findByOriginalUrl(originalUrl: String): ShortUrl?
}