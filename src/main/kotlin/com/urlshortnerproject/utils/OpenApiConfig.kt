package com.urlshortnerproject.utils

import io.swagger.v3.oas.models.Operation
import org.springdoc.core.customizers.OperationCustomizer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.HandlerMethod

@Configuration
class OpenApiConfig {

    @Value("\${api.shorten.summary}") lateinit var shortenSummary: String
    @Value("\${api.shorten.description}") lateinit var shortenDescription: String

    @Value("\${api.resolve.summary}") lateinit var resolveSummary: String
    @Value("\${api.resolve.description}") lateinit var resolveDescription: String

    @Value("\${api.getall.summary}") lateinit var getAllSummary: String
    @Value("\${api.getall.description}") lateinit var getAllDescription: String

    @Value("\${api.delete.summary}") lateinit var deleteSummary: String
    @Value("\${api.delete.description}") lateinit var deleteDescription: String

    @Bean
    fun customizeOperations(): OperationCustomizer {
        return OperationCustomizer { operation: Operation, handlerMethod: HandlerMethod ->
            when (handlerMethod.method.name) {
                "shorten" -> {
                    operation.summary = shortenSummary
                    operation.description = shortenDescription
                }
                "resolve" -> {
                    operation.summary = resolveSummary
                    operation.description = resolveDescription
                }
                "getAllUrls" -> {
                    operation.summary = getAllSummary
                    operation.description = getAllDescription
                }
                "delete" -> {
                    operation.summary = deleteSummary
                    operation.description = deleteDescription
                }
            }
            operation
        }
    }
}