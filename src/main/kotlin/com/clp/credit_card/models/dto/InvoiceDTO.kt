package com.clp.credit_card.models.dto

import com.clp.credit_card.entities.InvoiceEntity
import java.time.LocalDate

/**
 * Data transfer object representing an invoice response.
 *
 * @property value The value of the invoice.
 * @property dueDate The due date of the invoice.
 * @property closingDate The closing date of the invoice.
 * @property isPaid Indicates if the invoice is paid.
 * @property paymentDate The date the invoice was paid, if applicable.
 * @property cardId The ID of the associated credit card.
 */
data class InvoiceResponse(
    val id: Int,
    val value: Double,
    val dueDate: LocalDate,
    val closingDate: LocalDate,
    val isPaid: Boolean,
    val paymentDate: LocalDate?,
    val cardId: Int
)

/**
 * Extension function to convert an InvoiceEntity to an InvoiceResponse.
 *
 * @receiver InvoiceEntity The invoice entity to be converted.
 * @return InvoiceResponse The converted invoice response.
 */
fun InvoiceEntity.toInvoiceResponse(): InvoiceResponse {
    return InvoiceResponse(
        id = this.id.value,
        value = this.value,
        dueDate = this.dueDate,
        closingDate = this.closingDate,
        isPaid = this.isPaid,
        paymentDate = this.paymentDate,
        cardId = this.creditCard.id.value
    )
}

/**
 * Wrapper class for different types of invoice responses.
 */
sealed class InvoiceResponseWrapper {

    /**
     * Represents a response containing only an invoice.
     *
     * @property invoice The invoice response.
     */
    data class onlyInvoice(val invoice: InvoiceResponse) : InvoiceResponseWrapper()

    /**
     * Represents a response containing an invoice and associated purchases.
     *
     * @property invoice The invoice response.
     * @property purchases The associated purchases response.
     */
    data class withPurchase(val invoice: InvoiceResponse, val purchases: PurchaseResponseWrapper) :
        InvoiceResponseWrapper()
}