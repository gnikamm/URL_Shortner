package com.urlshortnerproject.service

import com.urlshortnerproject.dto.UrlMappingResponse
import com.urlshortnerproject.exception.UrlAlreadyExistsException
import com.urlshortnerproject.model.ShortUrl
import com.urlshortnerproject.model.ShortenStrategy
import com.urlshortnerproject.repository.UrlMappingRepository
import com.urlshortnerproject.utils.Base62Encoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class UrlShortenerService(
    private val repository: UrlMappingRepository
) {

    fun shortenUrl(originalUrl: String, strategy: ShortenStrategy): String {
        val existing = repository.findByOriginalUrl(originalUrl)
        if (existing != null) {
            throw UrlAlreadyExistsException("URL already exists")
        }

        val shortCode = when (strategy) {
            ShortenStrategy.UUID -> UUID.randomUUID().toString().take(8)
            ShortenStrategy.BASE62 -> Base62Encoder.encode(System.currentTimeMillis())
        }

        repository.save(ShortUrl(shortCode = shortCode, originalUrl = originalUrl, strategy = strategy))
        return shortCode
    }

    fun getOriginalUrl(shortCode: String): String? {
        return repository.findByShortCode(shortCode)?.originalUrl
            ?: throw IllegalArgumentException("Invalid short code")
    }

    fun getAllMappings(): List<UrlMappingResponse> {
        return repository.findAll().map { UrlMappingResponse(it.shortCode, it.originalUrl) }
    }
}