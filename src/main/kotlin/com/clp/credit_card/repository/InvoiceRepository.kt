package com.clp.credit_card.repository

import com.clp.credit_card.entities.InvoiceEntity
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

@Repository
class InvoiceRepository {

    fun createInvoice(invoice: InvoiceEntity) : InvoiceEntity {
        val newInvoice = InvoiceEntity.new {
            value = invoice.value
            dueDate = invoice.dueDate
            paymentDate = invoice.paymentDate
        }

        return newInvoice
    }

}