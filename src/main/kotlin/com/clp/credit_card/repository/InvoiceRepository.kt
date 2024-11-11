package com.clp.credit_card.repository

import com.clp.credit_card.entities.CreditCardEntity
import com.clp.credit_card.entities.InvoiceEntity
import com.clp.credit_card.entities.PurchaseEntity
import com.clp.credit_card.models.CreditCard
import com.clp.credit_card.tables.InvoiceTable
import com.clp.credit_card.tables.PurchaseTable
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.javatime.month
import org.jetbrains.exposed.sql.javatime.year
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.Month

@Repository
class InvoiceRepository {

    /**
     * Creates a new invoice for the given credit card with the specified value.
     *
     * @param creditCard The credit card for which the invoice is being created.
     * @param value The value of the invoice.
     * @return The newly created InvoiceEntity.
     * @throws IllegalArgumentException if the CreditCardEntity is not found.
     */
    fun createInvoice(creditCard: CreditCard, value: Double) : InvoiceEntity {
        val cardAsEntity = CreditCardEntity.findById(creditCard.id) ?: throw IllegalArgumentException("CreditCardEntity not found")
        val closingDate = LocalDate.of(LocalDate.now().year, LocalDate.now().month, creditCard.closingDay)

        val newInvoice = InvoiceEntity.new {
            this.value = value
            dueDate = closingDate.plusDays(5)
            this.closingDate = closingDate
            this.creditCard = cardAsEntity
        }

        return newInvoice
    }

    /**
     * Creates a new invoice for the given credit card with the specified value, closing month, and closing year.
     *
     * @param value The value of the invoice. If null, defaults to 0.0.
     * @param creditCard The credit card for which the invoice is being created.
     * @param closingMonth The month in which the invoice is closing.
     * @param closingYear The year in which the invoice is closing.
     * @return The newly created InvoiceEntity.
     * @throws IllegalArgumentException if the CreditCardEntity is not found.
     */
    fun createInvoice(value: Double?, creditCard: CreditCard, closingMonth: Month, closingYear: java.time.Year) : InvoiceEntity {
        val cardAsEntity = CreditCardEntity.findById(creditCard.id) ?: throw IllegalArgumentException("CreditCardEntity not found")
        val closingDate = LocalDate.of(closingYear.value, closingMonth, creditCard.closingDay)

        val newInvoice = InvoiceEntity.new {
            this.value = value?: 0.0
            dueDate = closingDate.plusDays(5)
            this.closingDate = closingDate
            this.creditCard = cardAsEntity
        }

        return newInvoice
    }

    fun addPurchaseToCurrentInvoice(purchase: PurchaseEntity, creditCard: CreditCard) {
        transaction {
            val currentInvoice = getCurrentInvoice(creditCard.id)
            if (currentInvoice != null) {
                purchase.invoice = currentInvoice
                currentInvoice.value += purchase.value
            } else {
                val newInvoice = createInvoice(creditCard, purchase.value)
                purchase.invoice = newInvoice
            }
        }
    }

    fun addPurchaseToInvoice(purchase: PurchaseEntity, creditCard: CreditCard, invoiceDate: LocalDate) {
        transaction {
            val invoice = getInvoiceByDate(creditCard.id, invoiceDate) ?: createInvoice(purchase.value, creditCard, invoiceDate.month, java.time.Year.of(invoiceDate.year))
            purchase.invoice = invoice
            invoice.value += purchase.value
        }
    }

    private fun getCurrentInvoice(creditCard: Int): InvoiceEntity? {
        println("Getting current invoice")
        val currentInvoice = InvoiceEntity.find { (InvoiceTable.isPaid eq false) and (InvoiceTable.cardId eq creditCard) }
            .orderBy(InvoiceTable.dueDate to SortOrder.DESC)
            .firstOrNull()
        println("Current invoice: $currentInvoice")
        return currentInvoice
    }

    private fun getInvoiceByDate(creditCard: Int, invoiceDate: LocalDate): InvoiceEntity? {
        return InvoiceEntity.find {
            (InvoiceTable.cardId eq creditCard) and
                    (InvoiceTable.closingDate.year() eq invoiceDate.year) and
                    (InvoiceTable.closingDate.month() eq invoiceDate.month.value)
        }.firstOrNull()
    }

    fun getAllInvoices(cardId: Int): List<InvoiceEntity> {
        return transaction {
            InvoiceEntity.find { InvoiceTable.cardId eq cardId }.toList()
        }
    }
}