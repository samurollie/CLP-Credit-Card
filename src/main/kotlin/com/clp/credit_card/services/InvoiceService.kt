package com.clp.credit_card.services

import com.clp.credit_card.entities.InvoiceEntity
import com.clp.credit_card.repository.InvoiceRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class InvoiceService {

    fun createInvoice(invoice: InvoiceEntity) : InvoiceEntity {
        println("Creating invoice")
        return InvoiceRepository().createInvoice(invoice)
    }

}