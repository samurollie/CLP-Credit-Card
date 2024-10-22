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
        val newCard = CreditCard(
            numeroCartao = createCreditCardNumber(),
            cvv = createCVV(),
            dataValidade = createExpirationDate(),
            limiteDisponivel = defineAvailableLimit(),
            status = StatusEnum.Ativo,
            limiteTotal = defineTotalLimit(),
            idUsuario = newUserId,
            idFatura = null
        )

        creditCardRepository.addCreditCard(newCard)
        println("Added Credit Card: $newCard")
        return newCard
    }
}
