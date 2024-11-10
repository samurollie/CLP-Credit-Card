package com.clp.credit_card.repository

import com.clp.credit_card.entities.InvoiceEntity
import com.clp.credit_card.entities.PurchaseEntity
import com.clp.credit_card.tables.CreditCards
import com.clp.credit_card.tables.InvoiceTable
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.*

@Repository
class InvoiceRepository {

    fun createInvoice() : InvoiceEntity {
        val today = LocalDate.now()
        val newInvoice = InvoiceEntity.new {
            value = 0.0
            dueDate = LocalDate.parse("2022-02-01")
            paymentDate = LocalDate.parse("2022-02-01")
            closingDate = LocalDate.parse("2022-02-01")
        }

        return newInvoice
    }

    fun createInvoice(value: Double, dueDate: LocalDate, paymentDate: LocalDate): InvoiceEntity {
        val newInvoice = InvoiceEntity.new {
            this.value = value
            this.dueDate = dueDate
            this.paymentDate = paymentDate
        }
        return newInvoice
    }

    private fun getCurrentInvoice(): InvoiceEntity? {
        println("Getting current invoice")
        val currentInvoice = InvoiceEntity.find { InvoiceTable.isPaid eq false }
            .orderBy(InvoiceTable.dueDate to SortOrder.DESC)
            .firstOrNull()
        println("Current invoice: $currentInvoice")
        return currentInvoice
    }

    fun addPurchaseToCurrentInvoice(purchase: PurchaseEntity) {
        transaction {
            val currentInvoice = getCurrentInvoice()
//            val purchase = PurchaseEntity.findById(purchase.id)
            if (currentInvoice != null) {
                purchase.invoice = currentInvoice
            } else {
                println("No current invoice found. Creating new invoice.")
                val newInvoice = createInvoice()
                println("New invoice created: $newInvoice")
                purchase.invoice = newInvoice
            }
        }
    }

    fun getInvoicesByUserId(userId: Int): List<InvoiceEntity> {
        val retorno = transaction {
            (InvoiceTable innerJoin CreditCards)
                .select(CreditCards.idUsuario, CreditCards.idFatura)
                .where { CreditCards.idUsuario eq userId }
                .orderBy(InvoiceTable.dueDate to SortOrder.DESC)
                .map { InvoiceEntity.wrapRow(it) }
        }
        println(retorno)
        return transaction {
            (InvoiceTable innerJoin CreditCards)
                .select (CreditCards.idUsuario, CreditCards.idFatura)
                .where { CreditCards.idUsuario eq userId }
                .orderBy(InvoiceTable.dueDate to SortOrder.DESC)
                .map { InvoiceEntity.wrapRow(it) }
        }
    }
}