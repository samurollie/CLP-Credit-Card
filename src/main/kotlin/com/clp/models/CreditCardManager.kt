package com.clp.models

import java.time.LocalDate

class CreditCardManager {

    fun criarCartao(user: String): CreditCard {
        // Instanciação do cartão
        return CreditCard(
            dataDeVencimento = LocalDate.now(),
            limite = calculateLimite(),
            codigoVerificador = generateCardCVV(),
            numeroCartao = generateCardNumber(),
            usuario = user
        )
    }
    // Method to generate a random card number (you'd typically implement this with a service)
    companion object {
        fun generateCardNumber(): String {
            // Generate and return a card number
            return "1234-5678-9012-3456"  // placeholder
        }

        fun generateCardCVV(): String{
            return "1234"
        }

        fun calculateLimite(): LocalDate {
            return LocalDate.now()
        }
    }
}