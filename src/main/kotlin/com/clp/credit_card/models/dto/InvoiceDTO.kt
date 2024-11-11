package com.clp.credit_card.models.dto

import com.clp.credit_card.entities.InvoiceEntity
import java.time.LocalDate

data class InvoiceResponse(
    val value: Double,
    val dueDate: LocalDate,
    val closingDate: LocalDate,
    val isPaid: Boolean,
    val paymentDate: LocalDate?,
    val cardId: Int
)

fun InvoiceEntity.toInvoiceResponse(): InvoiceResponse {
    return InvoiceResponse(
        value = this.value,
        dueDate = this.dueDate,
        closingDate = this.closingDate,
        isPaid = this.isPaid,
        paymentDate = this.paymentDate,
        cardId = this.creditCard.id.value
    )
}

sealed class InvoiceResponseWrapper {
    data class onlyInvoice(val invoice: InvoiceResponse) : InvoiceResponseWrapper()
    data class withPurchase(val invoice: InvoiceResponse, val purchases: PurchaseResponseWrapper) : InvoiceResponseWrapper()
}