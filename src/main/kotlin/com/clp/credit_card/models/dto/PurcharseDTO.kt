package com.clp.credit_card.models.dto

import com.clp.credit_card.entities.PurchaseEntity
import java.time.LocalDate

/**
 * Data Transfer Object for Purchase.
 *
 * @property value The value of the purchase.
 * @property description The description of the purchase.
 * @property creditCard The credit card number used for the purchase.
 * @property installments The number of installments for the purchase.
 */
data class PurchaseDTO(val value: Double, val description: String, val creditCard: Int, val installments: Int)

/**
 * Data Transfer Object for Purchase Response.
 *
 * @property id The unique identifier of the purchase.
 * @property value The value of the purchase.
 * @property date The date of the purchase.
 * @property description The description of the purchase.
 */
data class PurchaseResponse(val id: Int, val value: Double, val date: LocalDate, val description: String)

fun PurchaseEntity.toPurchaseResponse(): PurchaseResponse {
    return PurchaseResponse(
        id = this.id.value,
        value = this.value,
        date = this.date,
        description = this.description
    )
}

/**
 * Wrapper for PurchaseResponse.
 */
sealed class PurchaseResponseWrapper {

    /**
     * Represents a single purchase response.
     *
     * @property purchase The purchase response.
     */
    data class Single(val purchase: PurchaseResponse) : PurchaseResponseWrapper()

    /**
     * Represents multiple purchase responses.
     *
     * @property purchases The list of purchase responses.
     */
    data class Multiple(val purchases: List<PurchaseResponse>) : PurchaseResponseWrapper()
}