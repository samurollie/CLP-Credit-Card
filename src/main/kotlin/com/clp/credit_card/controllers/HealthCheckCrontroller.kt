package com.clp.credit_card.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.sql.DataSource

/**
 * REST controller for checking the health of the database connection.
 * Provides an endpoint to verify the connection status to the database.
 *
 * @param dataSource Injected DataSource to manage database connections.
 */
@RestController
@RequestMapping("/api/health")
class HealthCheckController(private val dataSource: DataSource) {

    /**
     * Endpoint to check the database connection health.
     *
     * @return ResponseEntity<String> indicating the connection status:
     * - HTTP 200 (OK) if the database connection is valid.
     * - HTTP 500 (Internal Server Error) if the connection fails.
     */
    @GetMapping("/db")
    fun checkDatabaseConnection(): ResponseEntity<String> {
        return try {
            dataSource.connection.use { connection ->
                if (connection.isValid(2)) {
                    ResponseEntity("Database connection is healthy!", HttpStatus.OK)
                } else {
                    ResponseEntity("Database connection is NOT healthy!", HttpStatus.INTERNAL_SERVER_ERROR)
                }
            }
        } catch (e: Exception) {
            ResponseEntity("Database connection failed: ${e.message}", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}
