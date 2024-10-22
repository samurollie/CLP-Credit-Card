package com.clp.controllers
import org.springframework.web.bind.annotation.*
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus

@RestController
@RequestMapping("/api/credit-card")
class CreditCardServices {
    @PostMapping("/create")
    fun createCreditCard(@RequestParam newUserId: Int): ResponseEntity<String> {
        return try {
            // Call the CreditCardServices.createCreditCard method
            creditCardServices.createCreditCard(newUserId)
            ResponseEntity("Credit card created successfully!", HttpStatus.OK)
        } catch (e: Exception) {
            ResponseEntity("Failed to create credit card: ${e.message}", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}