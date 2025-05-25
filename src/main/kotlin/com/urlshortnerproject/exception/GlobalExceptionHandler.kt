package com.urlshortnerproject.exception

import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.urlshortnerproject.dto.ErrorResponse
import com.urlshortnerproject.model.ShortenStrategy
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

// Global Exception Handler
@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(UrlAlreadyExistsException::class)
    fun handleUrlAlreadyExists(ex: UrlAlreadyExistsException) =
        ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse(ex.message ?: "URL already exists"))

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(ex: IllegalArgumentException) =
        ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse(ex.message ?: "Not found"))

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleBadRequest(ex: HttpMessageNotReadableException): ResponseEntity<ErrorResponse> {
        val rootCause = ex.cause
        if (rootCause is InvalidFormatException && rootCause.targetType == ShortenStrategy::class.java) {
            val invalidValue = rootCause.value
            val allowedValues = ShortenStrategy.entries.joinToString(", ")
            val message = "Invalid strategy: $invalidValue. Allowed: [$allowedValues]"
            return ResponseEntity.badRequest().body(ErrorResponse(message))
        }
        return ResponseEntity.badRequest().body(ErrorResponse("Malformed JSON request"))
    }
}