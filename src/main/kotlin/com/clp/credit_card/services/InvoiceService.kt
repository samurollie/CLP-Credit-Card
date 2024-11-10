package com.clp.credit_card.services

import com.clp.credit_card.repository.InvoiceRepository
import org.springframework.stereotype.Service

@Service
class InvoiceService (private val invoiceRepository: InvoiceRepository) {

    /*fun createInvoice() : InvoiceEntity {
        return this.invoiceRepository.createInvoice()
    }*/

}
