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
    /**
     * Retrieves all invoices with their associated purchases for a given credit card ID.
     *
     * @param cardId The ID of the credit card.
     * @return A list of InvoiceResponseWrapper objects containing invoices and their purchases.
     */
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

    /**
     * Retrieves all invoices for a given credit card ID.
     *
     * @param cardId The ID of the credit card.
     * @return A list of InvoiceResponseWrapper.onlyInvoice objects containing invoices.
     */
    fun getAllInvoices(cardId: Int): List<InvoiceResponseWrapper.onlyInvoice> {
        val response: MutableList<InvoiceResponseWrapper.onlyInvoice> = mutableListOf()
        val invoices = invoiceRepository.getAllInvoices(cardId)
        invoices.forEach {
            response.add(InvoiceResponseWrapper.onlyInvoice(it.toInvoiceResponse()))
        }

        return (response)
    }

    /**
     * Retrieves an invoice with its associated purchases for a given credit card ID, month, and year.
     *
     * @param cardId The ID of the credit card.
     * @param month The month of the invoice.
     * @param year The year of the invoice.
     * @return An InvoiceResponseWrapper object containing the invoice and its purchases.
     * @throws IllegalArgumentException if the invoice is not found.
     */
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

    /**
     * Retrieves an invoice for a given credit card ID, month, and year.
     *
     * @param cardId The ID of the credit card.
     * @param month The month of the invoice.
     * @param year The year of the invoice.
     * @return An InvoiceResponseWrapper object containing the invoice.
     * @throws IllegalArgumentException if the invoice is not found.
     */
    fun getInvoice(cardId: Int, month: Month, year: Year): InvoiceResponseWrapper {
        val invoice = invoiceRepository.getInvoiceByDate(cardId, month, year)
        if (invoice == null) {
            throw IllegalArgumentException("Invoice not found")
        }
        return InvoiceResponseWrapper.onlyInvoice(invoice.toInvoiceResponse())
    }

    /**
     * Marks an invoice as paid for a given invoice ID and credit card ID.
     *
     * @param id The ID of the invoice to be paid.
     * @param cardId The ID of the credit card associated with the invoice.
     * @throws IllegalArgumentException if the invoice is not found, already paid, or does not belong to the given credit card.
     */
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

    /**
     * Marks the current invoice as paid for a given credit card ID.
     *
     * @param cardId The ID of the credit card associated with the invoice.
     * @throws IllegalArgumentException if the invoice is not found or is already paid.
     */
    fun payCurrentInvoice(cardId: Int) {
        val invoice = invoiceRepository.getCurrentInvoice(cardId)
        if (invoice == null) {
            throw IllegalArgumentException("Invoice not found or is already paid")
        } else if (invoice.isPaid) {
            throw IllegalArgumentException("Invoice already paid")
        }
        invoiceRepository.payInvoice(invoice)
    }

    /**
     * Deletes all invoices and their associated purchases for a given credit card ID.
     *
     * @param cardId The ID of the credit card.
     * @throws IllegalArgumentException if no invoices are found for the given card ID.
     */
    fun deleteAllInvoicesByCardId(cardId: Int) {
        // Buscar todos os invoices do cartão com o ID fornecido
        val invoices = invoiceRepository.getAllInvoices(cardId)

        // Verificar se existem invoices para o cartão
        if (invoices.isEmpty()) {
            return
        }

        // Para cada invoice, excluir as compras associadas antes de excluir o invoice
        invoices.forEach { invoice ->
            purchaseRepository.deleteAllPurchasesByInvoice(invoice.id.value)
            invoiceRepository.deleteInvoiceById(invoice.id.value) // Exclui o invoice
        }
    }
}
