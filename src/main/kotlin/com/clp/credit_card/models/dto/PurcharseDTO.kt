package com.clp.credit_card.models.dto

/**
 * Data Transfer Object for Purchase.
 *
 * @property value The value of the purchase.
 * @property description The description of the purchase.
 * @property creditCard The credit card number used for the purchase.
 * @property installments The number of installments for the purchase.
 */
data class PurchaseDTO(val value: Double, val description: String, val creditCard: Int, val installments: Int)