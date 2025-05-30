package com.urlshortnerproject.service

import com.urlshortnerproject.dto.UrlMappingResponse
import com.urlshortnerproject.exception.UrlAlreadyExistsException
import com.urlshortnerproject.model.ShortUrl
import com.urlshortnerproject.model.ShortenStrategy
import com.urlshortnerproject.repository.UrlMappingRepository
import com.urlshortnerproject.utils.Base62Encoder
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

@Service
class UrlShortenerService(

    private val repository: UrlMappingRepository,

    @Value("\${error.url.exists}")
    private val urlExistsMessage: String,

    @Value("\${error.shortcode.invalid}")
    private val invalidShortCodeMessage: String,

    @Value("\${error.shortcode.notfound}")
    private val shortCodeNotFoundMessage: String

) {

    // Creates a short code for a new URL using the selected strategy
    fun shortenUrl(originalUrl: String, strategy: ShortenStrategy): String {
        repository.findByOriginalUrl(originalUrl)?.let {
            throw UrlAlreadyExistsException(urlExistsMessage)
        }

        val shortCode = generateShortCode(strategy)
        repository.save(ShortUrl(shortCode = shortCode, originalUrl = originalUrl, strategy = strategy))
        return shortCode
    }

    // Returns the original URL for a given short code; throws if invalid
    fun getOriginalUrl(shortCode: String): String {
        val shortUrl = repository.findByShortCode(shortCode)
            ?: throw IllegalArgumentException(invalidShortCodeMessage)
        return shortUrl.originalUrl
    }

    // Returns all URL mappings in the system
    fun getAllMappings(): List<UrlMappingResponse> {
        return repository.findAll().map {
            UrlMappingResponse(
                shortCode = it.shortCode,
                originalUrl = it.originalUrl
            )
        }
    }

    // Deletes a short code from the system; throws if not found
    fun deleteUrl(shortCode: String) {
        val shortUrl = repository.findByShortCode(shortCode)
            ?: throw IllegalArgumentException(shortCodeNotFoundMessage)
        repository.delete(shortUrl)
    }

    // Strategy-based code generation; UUID vs timestamp-based Base62
    private fun generateShortCode(strategy: ShortenStrategy): String {
        return when (strategy) {
            ShortenStrategy.UUID -> UUID.randomUUID().toString().take(8)
            ShortenStrategy.BASE62 -> Base62Encoder.encode(System.currentTimeMillis())
        }
    }
}