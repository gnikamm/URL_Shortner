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

    fun shortenUrl(originalUrl: String, strategy: ShortenStrategy): String {
        repository.findByOriginalUrl(originalUrl)?.let {
            throw UrlAlreadyExistsException(urlExistsMessage)
        }

        val shortCode = generateShortCode(strategy)
        repository.save(ShortUrl(shortCode = shortCode, originalUrl = originalUrl, strategy = strategy))
        return shortCode
    }

    fun getOriginalUrl(shortCode: String): String {
        val shortUrl = repository.findByShortCode(shortCode)
            ?: throw IllegalArgumentException(invalidShortCodeMessage)
        return shortUrl.originalUrl
    }

    fun getAllMappings(): List<UrlMappingResponse> {
        return repository.findAll().map {
            UrlMappingResponse(
                shortCode = it.shortCode,
                originalUrl = it.originalUrl
            )
        }
    }

    fun deleteUrl(shortCode: String) {
        val shortUrl = repository.findByShortCode(shortCode)
            ?: throw IllegalArgumentException(shortCodeNotFoundMessage)
        repository.delete(shortUrl)
    }

    private fun generateShortCode(strategy: ShortenStrategy): String {
        return when (strategy) {
            ShortenStrategy.UUID -> UUID.randomUUID().toString().take(8)
            ShortenStrategy.BASE62 -> Base62Encoder.encode(System.currentTimeMillis())
        }
    }
}