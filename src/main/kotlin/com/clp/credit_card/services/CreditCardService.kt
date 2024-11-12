package com.clp.credit_card.services

import com.clp.credit_card.models.CreditCard
import com.clp.credit_card.models.StatusEnum
import com.clp.credit_card.repository.CreditCardRepository
import com.clp.credit_card.repository.UserRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class CreditCardService(
    private val creditCardRepository: CreditCardRepository,
    private val userRepository: UserRepository,
    private val invoiceService: InvoiceService
) { // Dependency injection

    /**
     * Defines the total limit for a credit card.
     *
     * @return the total limit as a Double.
     */
    private fun defineTotalLimit(): Double {
        return 2000.0
    }

    /**
     * Defines the available limit for a credit card.
     *
     * @return the available limit as a Double.
     */
    private fun defineAvailableLimit(): Double {
        return 2000.0
    }

    /**
     * Generates a unique credit card number.
     *
     * @return a new unique credit card number as a String.
     */
    private fun createCreditCardNumber(): String {
        val prefix = "1234"
        var randomPart: String

        do {
            randomPart = (100000000000..999999999999).random().toString() // Random 12-digit number
        } while (creditCardRepository.existsCreditCardNumber("$prefix$randomPart")) // Repeat if number exists

        return "$prefix$randomPart"
    }

    /**
     * Creates an expiration date for the credit card.
     *
     * @return the expiration date as a LocalDate.
     */
    private fun createExpirationDate(): LocalDate {
        return LocalDate.now().plusYears(5).withMonth(12).withDayOfMonth(31)
    }

    private fun createCVV(): String {
        return (100..999).random().toString()  // Random 3-digit number
    }

    /**
     * Creates a new credit card for a user.
     *
     * @param newUserId the ID of the user for whom the credit card is being created.
     * @param closingDay the day of the month when the credit card statement closes.
     * @return the created CreditCard object.
     * @throws IllegalArgumentException if the user ID is invalid or if the limits are negative.
     * @throws RuntimeException if there is an error while adding the credit card to the repository.
     */
    fun createCreditCard(newUserId: Int, closingDay: Int): CreditCard {
        // Validate user ID
        if (newUserId <= 0) {
            throw IllegalArgumentException("Invalid user ID.")
        }

        // Create a new credit card instance
        val newCard = CreditCard(
            numeroCartao = createCreditCardNumber(),
            cvv = createCVV(),
            dataValidade = createExpirationDate(),
            limiteDisponivel = defineAvailableLimit().takeIf { it >= 0 }
                ?: throw IllegalArgumentException("Available limit must be non-negative."),
            status = StatusEnum.Ativo,
            limiteTotal = defineTotalLimit().takeIf { it >= 0 }
                ?: throw IllegalArgumentException("Total limit must be non-negative."),
            idUsuario = newUserId,
            closingDay = closingDay
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


    /**
     * Retrieves a credit card by its ID.
     *
     * @param id the ID of the credit card to retrieve.
     * @return the CreditCard object if found, or null if not found.
     * @throws IllegalArgumentException if the ID is not positive.
     */
    fun getCreditCardById(id: Int): CreditCard? {
        if (id <= 0) throw IllegalArgumentException("ID must be positive.")
        return creditCardRepository.getCreditCardById(id)
    }

    /**
     * Deletes a credit card by its ID.
     *
     * @param id the ID of the credit card to delete.
     * @return true if the credit card was successfully deleted, false otherwise.
     * @throws IllegalArgumentException if the ID is not positive.
     */
    fun deleteCreditCardById(id: Int): Boolean {
        if (id <= 0) throw IllegalArgumentException("ID must be positive.")
        invoiceService.deleteAllInvoicesByCardId(id)
        return creditCardRepository.deleteCreditCardById(id)
    }

    /**
     * Updates the total limit of a credit card.
     *
     * @param id the ID of the credit card to update.
     * @param newLimit the new total limit to set.
     * @throws IllegalArgumentException if the ID is not positive or if the new limit is negative.
     */
    fun updateCreditCardLimit(id: Int, newLimit: Double) {
        if (id <= 0) throw IllegalArgumentException("ID must be positive.")
        if (newLimit < 0) throw IllegalArgumentException("New limit must be non-negative.")
        return creditCardRepository.updateCreditLimit(id, newLimit)
    }

    /**
     * Updates the available limit of a credit card.
     *
     * @param id the ID of the credit card to update.
     * @param newLimit the new available limit to set.
     * @throws IllegalArgumentException if the ID is not positive or if the new limit is negative.
     */
    fun updateCreditCardAvailableLimit(id: Int, newLimit: Double) {
        if (id <= 0) throw IllegalArgumentException("ID must be positive.")
        if (newLimit < 0) throw IllegalArgumentException("New limit must be non-negative.")
        return creditCardRepository.updateAvailableLimit(id, newLimit)
    }

    /**
     * Updates the status of a credit card.
     *
     * @param id the ID of the credit card to update.
     * @param status the new status to set.
     * @throws IllegalArgumentException if the ID is not positive or if the status is the same as the current status.
     * @throws NoSuchElementException if the credit card is not found.
     */
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


    /**
     * Updates the expiration date of a credit card.
     *
     * @param id the ID of the credit card to update.
     * @param expirationDate the new expiration date to set.
     * @throws IllegalArgumentException if the ID is not positive or if the expiration date is in the past.
     */
    fun updateCreditCardExpirationDate(id: Int, expirationDate: LocalDate) {
        if (id <= 0) throw IllegalArgumentException("ID must be positive.")
        if (expirationDate.isBefore(LocalDate.now())) throw IllegalArgumentException("Expiration date cannot be in the past.")
        return creditCardRepository.updateCreditExpirationDate(id, expirationDate)
    }

    /**
     * Retrieves all credit cards.
     *
     * @return a list of all CreditCard objects.
     */
    fun getAllCreditCards(): List<CreditCard> {
        return creditCardRepository.getAllCreditCards()
    }

    /**
     * Creates a new user.
     *
     * @return the ID of the newly created user.
     */
    fun createUser(): Int {
        return userRepository.addUser()
    }
}
