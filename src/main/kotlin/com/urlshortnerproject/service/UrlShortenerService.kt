package com.urlshortnerproject.service

import com.urlshortnerproject.model.UrlMapping
import com.urlshortnerproject.repository.UrlMappingRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class UrlShortenerService(
    private val repository: UrlMappingRepository
) {

    fun shortenUrl(originalUrl: String): String {
        val shortCode = generateShortCode()
        repository.save(UrlMapping(shortCode = shortCode, originalUrl = originalUrl))
        return shortCode
    }

    fun getOriginalUrl(shortCode: String): String? {
        return repository.findByShortCode(shortCode)?.originalUrl
    }

    private fun generateShortCode(): String {
        return UUID.randomUUID().toString().take(8)
    }
}