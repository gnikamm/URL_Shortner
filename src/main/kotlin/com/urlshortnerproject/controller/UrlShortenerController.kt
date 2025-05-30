package com.urlshortnerproject.controller

import com.urlshortnerproject.dto.ResolveResponse
import com.urlshortnerproject.dto.ShortenRequest
import com.urlshortnerproject.dto.ShortenResponse
import com.urlshortnerproject.service.UrlShortenerService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
@Tag(name = "\${api.tag.name}", description = "\${api.tag.description}")
class UrlShortenerController(

        private val service: UrlShortenerService,

        @Value("\${error.resolve.not-found}")
        private val resolveNotFoundMessage: String,

        @Value("\${error.urls.not-found}")
        private val urlsNotFoundMessage: String,

        @Value("\${api.delete.success.message}")
        private val deletedUrlMessage: String

    ) {

    // Accepts a long URL and shortening strategy, returns a generated short code
    @PostMapping("/shorten-url")
    fun shorten(@RequestBody request: ShortenRequest) : ResponseEntity<ShortenResponse> {
        val code = service.shortenUrl(request.url, request.strategy)
        return ResponseEntity.ok(ShortenResponse(code))
    }

    // Resolves the original URL for a given short code; returns 404 if not found
    @GetMapping("/original-url/{code}")
    fun resolve(@PathVariable code: String): ResponseEntity<ResolveResponse> {
        return try {
            val originalUrl = service.getOriginalUrl(code)
            ResponseEntity.ok(ResolveResponse(originalUrl))
        } catch (ex: IllegalArgumentException) {
            val message = String.format(resolveNotFoundMessage, code)
            ResponseEntity.status(404).body(ResolveResponse(message))
        }
    }

    // Returns all stored URL mappings; returns 404 if empty
    @GetMapping("/urls")
    fun getAllUrls(): ResponseEntity<Any> {
        val mappings = service.getAllMappings()
        return if (mappings.isEmpty()) {
            ResponseEntity.status(404).body(mapOf("error" to urlsNotFoundMessage))
        } else {
            ResponseEntity.ok(mappings)
        }
    }

    // Deletes a URL mapping based on the short code; returns 404 if not found
    @DeleteMapping("/delete-url/{code}")
    fun delete(@PathVariable code: String): ResponseEntity<Map<String, String>> {
       return  try {
           service.deleteUrl(code)
           val formattedMessage = String.format(deletedUrlMessage, code)
           ResponseEntity.ok(mapOf("message" to formattedMessage))
        } catch (ex: IllegalArgumentException) {
            ResponseEntity.status(404).body(mapOf("error" to ex.message.toString()))
        }
    }
}