package com.clp.credit_card.models

import java.time.LocalDate

/**
 * Enum representing the status of a credit card.
 */
enum class StatusEnum {
    Ativo,      // Active
    Bloqueado,  // Blocked
    Cancelado   // Canceled
}

/**
 * Data class representing a Credit Card.
 *
 * @property id The unique identifier of the credit card.
 * @property numeroCartao The card number.
 * @property cvv The card verification value.
 * @property dataValidade The expiration date of the card.
 * @property limiteDisponivel The available credit limit.
 * @property status The status of the credit card.
 * @property limiteTotal The total credit limit.
 * @property idUsuario The user ID associated with the credit card.
 * @property closingDay The day of the month when the credit card statement closes.
 */
data class CreditCard(
    var id: Int = 0,
    val numeroCartao: String,
    val cvv: String,
    val dataValidade: LocalDate,
    val limiteDisponivel: Double,
    val status: StatusEnum,
    val limiteTotal: Double,
    val idUsuario: Int,
    val closingDay: Int,
)

