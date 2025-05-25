package com.urlshortnerproject.controller

import com.urlshortnerproject.dto.ResolveResponse
import com.urlshortnerproject.dto.ShortenRequest
import com.urlshortnerproject.dto.ShortenResponse
import com.urlshortnerproject.service.UrlShortenerService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class UrlShortenerController(private val service: UrlShortenerService) {

    @PostMapping("/shorten-url")
    fun shorten(@RequestBody request: ShortenRequest) =
        ResponseEntity.ok(ShortenResponse(service.shortenUrl(request.url, request.strategy)))

    @GetMapping("/original-url/{code}")
    fun resolve(@PathVariable code: String) =
        runCatching { service.getOriginalUrl(code) }
            .map { ResponseEntity.ok(ResolveResponse(it)) }
            .getOrElse { ResponseEntity.status(404).body(ResolveResponse("")) }

    @GetMapping("/urls")
    fun getAllUrls() = ResponseEntity.ok(service.getAllMappings())
}