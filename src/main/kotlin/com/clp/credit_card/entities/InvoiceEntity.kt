package com.clp.credit_card.entities

import com.clp.credit_card.tables.InvoiceTable
import com.clp.credit_card.tables.PurchaseTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class InvoiceEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object: IntEntityClass<InvoiceEntity>(InvoiceTable)

    var value by InvoiceTable.value
    var dueDate by InvoiceTable.dueDate
    var closingDate by InvoiceTable.closingDate
    var isPaid by InvoiceTable.isPaid
    var paymentDate by InvoiceTable.paymentDate
    var creditCard by CreditCardEntity referencedOn InvoiceTable.cardId // Refers to the credit card that this invoice belongs to
    val purchases by PurchaseEntity referrersOn PurchaseTable.invoiceId // Refere-se a todas as compras feitas nessa fatura
}