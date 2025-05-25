package com.urlshortnerproject.exception

import com.urlshortnerproject.dto.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

// Global Exception Handler
@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(UrlAlreadyExistsException::class)
    fun handleUrlAlreadyExists(ex: UrlAlreadyExistsException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse(ex.message ?: "URL already exists"))
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(ex: IllegalArgumentException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse(ex.message ?: "Not found"))
    }
}