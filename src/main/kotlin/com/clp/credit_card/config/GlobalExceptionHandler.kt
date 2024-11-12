package com.clp.credit_card.config

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.server.ResponseStatusException

@ControllerAdvice
class GlobalExceptionHandler {

    /**
 * Handles `ResponseStatusException` and returns a `ResponseEntity` with the appropriate status code and message.
 *
 * @param e the `ResponseStatusException` to handle
 * @return a `ResponseEntity` containing the status code and message
 */
@ExceptionHandler(ResponseStatusException::class)
fun handleResponseStatusException(e: ResponseStatusException): ResponseEntity<String> {
        val messageParts = e.message.substringAfter("\"")?.substringBefore("\"")
        val body = messageParts ?: "Unknown error"
        return ResponseEntity.status(e.statusCode).body(body)
    }

/**
     * Handles generic `Exception` and returns a `ResponseEntity` with a status code of 500 and the error message.
     *
     * @param e the `Exception` to handle
     * @return a `ResponseEntity` containing the status code and message
     */
    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<String> {
        val messageParts = e.message?.substringAfter("\"")?.substringBefore("\"")
        val body = messageParts ?: "Unknown error"
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body)
    }
}