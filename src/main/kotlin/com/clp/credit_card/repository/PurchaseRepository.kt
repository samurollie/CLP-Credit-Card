package com.clp.credit_card.repository

import com.clp.credit_card.entities.PurchaseEntity
import com.clp.credit_card.tables.PurchaseTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class PurchaseRepository {

    fun createPurchase(value: Double, purchaseDate: LocalDate, description: String): PurchaseEntity {
        println("Creating purchase with value $value, date $purchaseDate, and description $description")
        return PurchaseEntity.new {
            this.value = value
            this.data = purchaseDate
            this.description = description
        }
    }

}