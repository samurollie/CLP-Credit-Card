package com.clp.credit_card.services

import com.clp.credit_card.models.dto.InvoiceResponseWrapper
import com.clp.credit_card.models.dto.PurchaseResponseWrapper
import com.clp.credit_card.models.dto.toInvoiceResponse
import com.clp.credit_card.models.dto.toPurchaseResponse
import com.clp.credit_card.repository.CreditCardRepository
import com.clp.credit_card.repository.InvoiceRepository
import com.clp.credit_card.repository.PurchaseRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Month
import java.time.Year

@Service
@Transactional
class InvoiceService(
    private val invoiceRepository: InvoiceRepository,
    private val purchaseRepository: PurchaseRepository,
    private val creditCardRepository: CreditCardRepository
) {
    fun getAllInvoicesWithPurchases(cardId: Int): List<InvoiceResponseWrapper> {
        val response: MutableList<InvoiceResponseWrapper.withPurchase> = mutableListOf()
        val invoices = invoiceRepository.getAllInvoices(cardId)
        invoices.forEach {
            val purchases = purchaseRepository.getAllPurchasesByInvoice(it.id.value)

            response.add(
                InvoiceResponseWrapper.withPurchase(
                    it.toInvoiceResponse(),
                    PurchaseResponseWrapper.Multiple(purchases.map { it.toPurchaseResponse() })
                )
            )
        }

        return (response)
    }

    fun getAllInvoices(cardId: Int): List<InvoiceResponseWrapper.onlyInvoice> {
        val response: MutableList<InvoiceResponseWrapper.onlyInvoice> = mutableListOf()
        val invoices = invoiceRepository.getAllInvoices(cardId)
        invoices.forEach {
            response.add(InvoiceResponseWrapper.onlyInvoice(it.toInvoiceResponse()))
        }

        return (response)
    }

    fun getInvoiceWithPurchases(cardId: Int, month: Month, year: Year): InvoiceResponseWrapper {
        val invoice = invoiceRepository.getInvoiceByDate(cardId, month, year)
        if (invoice == null) {
            throw IllegalArgumentException("Invoice not found")
        }
        val purchases = purchaseRepository.getAllPurchasesByInvoice(invoice.id.value)
        return InvoiceResponseWrapper.withPurchase(
            invoice.toInvoiceResponse(),
            PurchaseResponseWrapper.Multiple(purchases.map { it.toPurchaseResponse() })
        )
    }

    fun getInvoice(cardId: Int, month: Month, year: Year): InvoiceResponseWrapper {
        val invoice = invoiceRepository.getInvoiceByDate(cardId, month, year)
        if (invoice == null) {
            throw IllegalArgumentException("Invoice not found")
        }
        return InvoiceResponseWrapper.onlyInvoice(invoice.toInvoiceResponse())
    }

    fun payInvoice(id: Int, cardId: Int) {
        val invoice = invoiceRepository.getInvoiceById(id)

        if (invoice == null) {
            throw IllegalArgumentException("Invoice not found")
        } else if (invoice.isPaid) {
            throw IllegalArgumentException("Invoice already paid")
        }

        val creditCard = creditCardRepository.getCreditCardById(cardId)
        if (creditCard == null) {
            throw IllegalArgumentException("Credit card not found")
        }
        if (invoice.creditCard.id.value != creditCard.id) {
            throw IllegalArgumentException("Invoice does not belong to this credit card")
        }

        invoiceRepository.payInvoice(invoice)
        creditCardRepository.updateCreditLimit(creditCard.id, invoice.value + creditCard.limiteDisponivel)
    }

    fun payCurrentInvoice(cardId: Int) {
        val invoice = invoiceRepository.getCurrentInvoice(cardId)
        if (invoice == null) {
            throw IllegalArgumentException("Invoice not found or is already paid")
        } else if (invoice.isPaid) {
            throw IllegalArgumentException("Invoice already paid")
        }
        invoiceRepository.payInvoice(invoice)
    }
}
