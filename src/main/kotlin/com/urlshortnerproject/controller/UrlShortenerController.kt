package com.urlshortnerproject.controller

import com.urlshortnerproject.dto.ResolveResponse
import com.urlshortnerproject.dto.ShortenRequest
import com.urlshortnerproject.dto.ShortenResponse
import com.urlshortnerproject.service.UrlShortenerService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
@Tag(name = "ShortenUrl" , description = "Shorten url using UUID or BASE62")
class UrlShortenerController(private val service: UrlShortenerService) {
    @Operation(summary = "Shorten a URL", description = "Shortens a given URL using the specified strategy (UUID or BASE62)")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Successfully shortened URL")
    )
    @PostMapping("/shorten-url")
    fun shorten(@RequestBody request: ShortenRequest) =
        ResponseEntity.ok(ShortenResponse(service.shortenUrl(request.url, request.strategy)))

    @Operation(summary = "Resolve shortened URL", description = "Retrieves the original URL by its short code")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Successfully retrieved original URL"),
        ApiResponse(responseCode = "404", description = "Short code not found")
    )
    @GetMapping("/original-url/{code}")
    fun resolve(@PathVariable code: String) =
        runCatching { service.getOriginalUrl(code) }
            .map { ResponseEntity.ok(ResolveResponse(it)) }
            .getOrElse { ResponseEntity.status(404).body(ResolveResponse("")) }

    @Operation(summary = "Get all shortened URLs", description = "Returns all stored URL mappings")
    @ApiResponse(responseCode = "200", description = "List of all mappings returned")
    @GetMapping("/urls")
    fun getAllUrls() = ResponseEntity.ok(service.getAllMappings())

    @Operation(summary = "Delete a shortened URL", description = "Deletes a specific shortened URL by its code")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Successfully deleted URL"),
        ApiResponse(responseCode = "404", description = "Short code not found")
    )
    @DeleteMapping("/delete-url/{code}")
    fun delete(@PathVariable code: String): ResponseEntity<Map<String, String>> =
        try {
            service.deleteUrl(code)
            ResponseEntity.ok(mapOf("message" to "URL with code '$code' has been deleted"))
        } catch (ex: IllegalArgumentException) {
            ResponseEntity.status(404).body(mapOf("error" to ex.message.toString()))
        }
}