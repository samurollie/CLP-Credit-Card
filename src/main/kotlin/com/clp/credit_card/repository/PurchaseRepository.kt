package com.clp.credit_card.repository

import com.clp.credit_card.entities.PurchaseEntity
import com.clp.credit_card.tables.PurchaseTable
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class PurchaseRepository {

    fun createPurchase(value: Double, purchaseDate: LocalDate, description: String): PurchaseEntity {
        println("Creating purchase with value $value, date $purchaseDate, and description $description")
        return PurchaseEntity.new {
            this.value = value
            this.date = purchaseDate
            this.description = description
        }
    }

    fun getAllPurchasesByInvoice(invoiceId: Int): List<PurchaseEntity> {
        return transaction {
            PurchaseEntity.find { PurchaseTable.invoiceId eq invoiceId }.toList()
        }
    }

    fun deleteAllPurchasesByInvoice(invoiceId: Int) {
        transaction {
            // Buscar todas as compras associadas ao invoiceId
            val purchases = PurchaseEntity.find { PurchaseTable.invoiceId eq invoiceId }

            // Excluir todas as compras
            purchases.forEach { it.delete() }
        }
    }
}