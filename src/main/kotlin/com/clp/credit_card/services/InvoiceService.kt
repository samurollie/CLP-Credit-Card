package com.clp.credit_card.services

import com.clp.credit_card.entities.InvoiceEntity
import com.clp.credit_card.models.dto.InvoiceResponseWrapper
import com.clp.credit_card.models.dto.PurchaseResponseWrapper
import com.clp.credit_card.models.dto.toInvoiceResponse
import com.clp.credit_card.models.dto.toPurchaseResponse
import com.clp.credit_card.repository.InvoiceRepository
import com.clp.credit_card.repository.PurchaseRepository
import org.springframework.stereotype.Service

@Service
class InvoiceService(
    private val invoiceRepository: InvoiceRepository,
    private val purchaseRepository: PurchaseRepository
) {
    fun getAllInvoicesWithPurchases(cardId: Int): List<InvoiceResponseWrapper> {
        val response : MutableList<InvoiceResponseWrapper.withPurchase> = mutableListOf()
        val invoices = invoiceRepository.getAllInvoices(cardId)
        invoices.forEach {
            val purchases = purchaseRepository.getAllPurchasesByInvoice(it.id.value)

            response.add(InvoiceResponseWrapper.withPurchase(it.toInvoiceResponse(), PurchaseResponseWrapper.Multiple(purchases.map { it.toPurchaseResponse() })))
        }

        return (response)
    }

    fun getAllInvoices(cardId: Int): List<InvoiceResponseWrapper.onlyInvoice> {
        val response : MutableList<InvoiceResponseWrapper.onlyInvoice> = mutableListOf()
        val invoices = invoiceRepository.getAllInvoices(cardId)
        invoices.forEach {
            response.add(InvoiceResponseWrapper.onlyInvoice(it.toInvoiceResponse()))
        }

        return (response)
    }

    fun getInvoiceWithPurchases(cardId: Int, month: Month, year: Year): InvoiceResponseWrapper {
        //Parei aqui
    }
}
