package com.clp.services

import com.clp.models.CreditCard
import com.clp.models.StatusEnum
import com.clp.repository.CreditCardRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class CreditCardServices(private val creditCardRepository: CreditCardRepository) { // Dependency injection

    private fun defineTotalLimit(): Double {
        return 2000.0
    }

    private fun defineAvailableLimit(): Double {
        return 1000.0
    }

    private fun createCreditCardNumber(): String {
        val prefix = "1234"
        var randomPart: String

        do {
            randomPart = (100000000000..999999999999).random().toString() // Random 12-digit number
        } while (creditCardRepository.existsCreditCardNumber("$prefix$randomPart")) // Repeat if number exists

        return "$prefix$randomPart"
    }

    private fun createExpirationDate(): LocalDate {
        return LocalDate.now().plusYears(5).withMonth(12).withDayOfMonth(31)
    }

    private fun createCVV(): String {
        return (100..999).random().toString()  // Random 3-digit number
    }

    fun createCreditCard(newUserId: Int): CreditCard {
        // Validate user ID
        if (newUserId <= 0) {
            throw IllegalArgumentException("Invalid user ID.")
        }

        // Create a new credit card instance
        val newCard = CreditCard(
            numeroCartao = createCreditCardNumber() ?: throw IllegalStateException("Failed to generate credit card number."),
            cvv = createCVV() ?: throw IllegalStateException("Failed to generate CVV."),
            dataValidade = createExpirationDate() ?: throw IllegalStateException("Failed to generate expiration date."),
            limiteDisponivel = defineAvailableLimit().takeIf { it >= 0 } ?: throw IllegalArgumentException("Available limit must be non-negative."),
            status = StatusEnum.Ativo,
            limiteTotal = defineTotalLimit().takeIf { it >= 0 } ?: throw IllegalArgumentException("Total limit must be non-negative."),
            idUsuario = newUserId,
            idFatura = null
        )

        // Attempt to add the credit card to the repository
        return try {
            creditCardRepository.addCreditCard(newCard)
            println("Added Credit Card: $newCard")
            newCard // Return the created card
        } catch (e: Exception) {
            throw RuntimeException("Failed to create credit card in the database: ${e.message}", e)
        }
    }


    fun getCreditCardById(id: Int): CreditCard? {
        if (id <= 0) throw IllegalArgumentException("ID must be positive.")
        return creditCardRepository.getCreditCardById(id)
    }

    fun deleteCreditCardById(id: Int): Boolean {
        if (id <= 0) throw IllegalArgumentException("ID must be positive.")
        return creditCardRepository.deleteCreditCardById(id)
    }

    fun updateCreditCardLimit(id: Int, newLimit: Double) {
        if (id <= 0) throw IllegalArgumentException("ID must be positive.")
        if (newLimit < 0) throw IllegalArgumentException("New limit must be non-negative.")
        return creditCardRepository.updateCreditLimit(id, newLimit)
    }

    fun updateCreditCardStatus(id: Int, status: StatusEnum) {
        if (id <= 0) throw IllegalArgumentException("ID must be positive.")

        // Fetch the current status of the credit card
        val currentCreditCard = creditCardRepository.getCreditCardById(id)
            ?: throw NoSuchElementException("Credit card not found.")

        // Check if the current status is the same as the new status
        if (currentCreditCard.status == status) {
            throw IllegalArgumentException("Credit card is already in the status: $status.")
        }

        // Proceed with the update if the status is different
        creditCardRepository.updateCreditStatus(id, status)
    }


    fun updateCreditCardExpirationDate(id: Int, expirationDate: LocalDate) {
        if (id <= 0) throw IllegalArgumentException("ID must be positive.")
        if (expirationDate.isBefore(LocalDate.now())) throw IllegalArgumentException("Expiration date cannot be in the past.")
        return creditCardRepository.updateCreditExpirationDate(id, expirationDate)
    }

    fun getAllCreditCards() : List<CreditCard> {
        return creditCardRepository.getAllCreditCards()
    }
}
