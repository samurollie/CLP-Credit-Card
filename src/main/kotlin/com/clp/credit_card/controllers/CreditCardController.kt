package com.clp.credit_card.controllers
import com.clp.credit_card.models.CreditCard
import com.clp.credit_card.models.StatusEnum
import org.springframework.web.bind.annotation.*
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus
import com.clp.credit_card.services.CreditCardServices
import java.time.LocalDate

@RestController
@RequestMapping("/api/credit-card")
class CreditCardController(private val creditCardServices: CreditCardServices) {

    @PostMapping("")
    fun createCreditCard(@RequestParam newUserId: Int): ResponseEntity<CreditCard> {
        return try {
            val newCard = creditCardServices.createCreditCard(newUserId)
            ResponseEntity.status(HttpStatus.CREATED).body(newCard) // Return the created credit card
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(null) // 400 Bad Request
        } catch (e: NoSuchElementException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) // 404 Not Found
        } catch (e: RuntimeException) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null) // 500 Internal Server Error
        }
    }

    @PostMapping("/create_user")
    fun createUser(): ResponseEntity<Int> {
        return try {
            val newUser = creditCardServices.createUser()
            ResponseEntity.status(HttpStatus.CREATED).body(newUser) // Return the created credit card
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(null) // 400 Bad Request
        } catch (e: NoSuchElementException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) // 404 Not Found
        } catch (e: RuntimeException) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null) // 500 Internal Server Error
        }
    }


    @GetMapping("/{id}")
    fun getCreditCard(@PathVariable id: Int): ResponseEntity<CreditCard> {
        return try {
            val creditCard = creditCardServices.getCreditCardById(id)
            // Check if creditCard is null
            if (creditCard != null) {
                ResponseEntity.ok(creditCard)
            } else {
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) // Card not found
            }
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(null) // Return 400 for invalid ID
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null) // Generic error handling
        }
    }


    @DeleteMapping("/{id}")
    fun deleteCreditCard(@PathVariable id: Int): ResponseEntity<CreditCard> {
        return try {
            // Attempt to retrieve the credit card info before deletion
            val creditCard = creditCardServices.getCreditCardById(id)
                ?: throw NoSuchElementException("Credit card not found.")

            // Attempt to delete the credit card
            creditCardServices.deleteCreditCardById(id)

            // Return the deleted credit card info
            ResponseEntity.ok(creditCard)
        } catch (e: IllegalArgumentException) {
            // Handle invalid ID error
            ResponseEntity.badRequest().body(null) // Return 400 Bad Request
        } catch (e: NoSuchElementException) {
            // Handle not found error
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) // Return 404 Not Found
        } catch (e: Exception) {
            // Handle any other unexpected errors
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null) // Return 500 Internal Server Error
        }
    }

    @PatchMapping("/{id}/limit")
    fun updateCreditLimit(
        @PathVariable id: Int,
        @RequestParam limiteTotal: Double // Era pra ser o total :(
    ): ResponseEntity<String> {
        return try {
            creditCardServices.updateCreditCardLimit(id, limiteTotal)
            ResponseEntity.ok("Credit limit updated successfully!")
        } catch (e: IllegalArgumentException) {
            // Handle cases like invalid ID or negative limit
            ResponseEntity.badRequest().body(e.message) // 400 Bad Request
        } catch (e: NoSuchElementException) {
            // Handle case where credit card is not found
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("Credit card not found") // 404 Not Found
        } catch (e: Exception) {
            // General error handling
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: ${e.message}") // 500 Internal Server Error
        }
    }


    @PatchMapping("/{id}/status")
    fun updateCardStatus(
        @PathVariable id: Int,
        @RequestParam status: StatusEnum
    ): ResponseEntity<String> {
        return try {
            creditCardServices.updateCreditCardStatus(id, status)
            ResponseEntity.ok("Card status updated successfully!")
        } catch (e: IllegalArgumentException) {
            // Handle the case where the ID is invalid or the status is already the same
            ResponseEntity.badRequest().body(e.message)
        } catch (e: NoSuchElementException) {
            // Handle the case where the credit card was not found
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("Credit card not found")
        }
    }


    @PatchMapping("/{id}/expiration")
    fun updateExpirationDate(
        @PathVariable id: Int,
        @RequestParam dataValidade: LocalDate
    ): ResponseEntity<String> {
        return try {
            creditCardServices.updateCreditCardExpirationDate(id, dataValidade)
            ResponseEntity.ok("Expiration date updated successfully!")
        } catch (e: IllegalArgumentException) {
            // Handle invalid input (e.g., ID must be positive or expiration date in the past)
            ResponseEntity.badRequest().body(e.message) // Return 400 Bad Request
        } catch (e: NoSuchElementException) {
            // Handle case where credit card is not found
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("Credit card not found")
        }
    }

    @GetMapping("/all")
    fun getAllCreditCards(): ResponseEntity<List<CreditCard>> {
        return try {
            val creditCards = creditCardServices.getAllCreditCards()
            ResponseEntity.ok(creditCards)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(emptyList())
        }
    }
}
