package com.clp.credit_card.entities

import com.clp.credit_card.tables.InvoiceTable
import com.clp.credit_card.tables.PurchaseTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

/**
 * Represents an invoice entity.
 *
 * @property value The value of the invoice.
 * @property dueDate The due date of the invoice.
 * @property closingDate The closing date of the invoice.
 * @property isPaid Indicates if the invoice is paid.
 * @property paymentDate The payment date of the invoice.
 * @property creditCard The credit card associated with this invoice.
 * @property purchases The purchases made on this invoice.
 */
class InvoiceEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<InvoiceEntity>(InvoiceTable)

    var value by InvoiceTable.value
    var dueDate by InvoiceTable.dueDate
    var closingDate by InvoiceTable.closingDate
    var isPaid by InvoiceTable.isPaid
    var paymentDate by InvoiceTable.paymentDate
    var creditCard by CreditCardEntity referencedOn InvoiceTable.cardId // Refers to the credit card that this invoice belongs to
    val purchases by PurchaseEntity referrersOn PurchaseTable.invoiceId // Refers to all purchases made on this invoice
}