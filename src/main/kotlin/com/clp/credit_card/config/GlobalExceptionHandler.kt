package com.clp.credit_card.config

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.server.ResponseStatusException

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException::class)
    fun handleResponseStatusException(e: ResponseStatusException): ResponseEntity<String> {
        val messageParts = e.message?.substringAfter("\"")?.substringBefore("\"")
        val body = messageParts ?: "Unknown error"
        return ResponseEntity.status(e.statusCode).body(body)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<String> {
        val messageParts = e.message?.substringAfter("\"")?.substringBefore("\"")
        val body = messageParts ?: "Unknown error"
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body)
    }
}