package com.clp.credit_card.services

import com.clp.credit_card.entities.PurchaseEntity
import com.clp.credit_card.repository.InvoiceRepository
import com.clp.credit_card.repository.PurchaseRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
@Transactional
class PurchaseService(
    private val purchaseRepository: PurchaseRepository,
    private val invoiceRepository: InvoiceRepository
) {
    fun createPurchaseAndAddToInvoice(value: Double, purchaseDate: LocalDate, description: String): PurchaseEntity {
        val purchase = purchaseRepository.createPurchase(value, purchaseDate, description)
        println("Purchase created: $purchase")
        invoiceRepository.addPurchaseToCurrentInvoice(purchase)
        println("Purchase added to invoice")
        return purchase
    }
}