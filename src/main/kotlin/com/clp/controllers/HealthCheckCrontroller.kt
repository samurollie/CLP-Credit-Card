package com.clp.controllers

import org.springframework.web.bind.annotation.*
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus
import javax.sql.DataSource
import java.sql.Connection

@RestController
@RequestMapping("/api/health")
class HealthCheckController(private val dataSource: DataSource) { // Inject the DataSource

    @GetMapping("/db")
    fun checkDatabaseConnection(): ResponseEntity<String> {
        return try {
            dataSource.connection.use { connection -> // Try to get a connection from the DataSource
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
