package com.urlshortnerproject.controller

import com.urlshortnerproject.service.UrlShortenerService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class UrlShortenerController(
    private val service: UrlShortenerService
) {

    data class ShortenRequest(val url: String)
    data class ShortenResponse(val shortCode: String)

    @PostMapping("/shorten")
    fun shorten(@RequestBody request: ShortenRequest): ResponseEntity<ShortenResponse> {
        val code = service.shortenUrl(request.url)
        return ResponseEntity.ok(ShortenResponse(code))
    }

    @GetMapping("/{code}")
    fun resolve(@PathVariable code: String): ResponseEntity<String> {
        val original = service.getOriginalUrl(code)
        return if (original != null) ResponseEntity.ok(original)
        else ResponseEntity.notFound().build()
    }
}