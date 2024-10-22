package com.clp.controllers
import org.springframework.web.bind.annotation.*
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus
import com.clp.services.CreditCardServices

@RestController
@RequestMapping("/api/credit-card")
class CreditCardController(private val creditCardServices: CreditCardServices) {

    @PostMapping("/create")
    fun createCreditCard(@RequestParam newUserId: Int): ResponseEntity<String> { // Change back to String
        return try {
            // Call the CreditCardServices.createCreditCard method
            creditCardServices.createCreditCard(newUserId)
            ResponseEntity("Credit card created successfully!", HttpStatus.CREATED) // Return success message
        } catch (e: Exception) {
            ResponseEntity("Failed to create credit card: ${e.message}", HttpStatus.INTERNAL_SERVER_ERROR) // Return error message
        }
    }
}
