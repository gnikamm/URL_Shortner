package com.urlshortnerproject.controller

import com.urlshortnerproject.dto.ResolveResponse
import com.urlshortnerproject.dto.ShortenRequest
import com.urlshortnerproject.dto.ShortenResponse
import com.urlshortnerproject.dto.UrlMappingResponse
import com.urlshortnerproject.service.UrlShortenerService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class UrlShortenerController(
    private val service: UrlShortenerService
) {

    @PostMapping("/shorten-url")
    fun shorten(@RequestBody request: ShortenRequest): ResponseEntity<ShortenResponse> {
        val code = service.shortenUrl(request.url, request.strategy)
        return ResponseEntity.ok(ShortenResponse(code))
    }

    @GetMapping("/original-url/{code}")
    fun resolve(@PathVariable code: String): ResponseEntity<ResolveResponse> {
        return try {
            val original = service.getOriginalUrl(code)
            ResponseEntity.ok(ResolveResponse(original))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResolveResponse(""))
        }
    }

    @GetMapping("/urls")
    fun getAllUrls(): ResponseEntity<List<UrlMappingResponse>> {
        val mappings = service.getAllMappings()
        return ResponseEntity.ok(mappings)
    }
}