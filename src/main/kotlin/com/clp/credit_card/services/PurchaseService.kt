package com.clp.credit_card.services

import com.clp.credit_card.entities.PurchaseEntity
import com.clp.credit_card.models.dto.PurchaseDTO
import com.clp.credit_card.repository.CreditCardRepository
import com.clp.credit_card.repository.InvoiceRepository
import com.clp.credit_card.repository.PurchaseRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
@Transactional
class PurchaseService(
    private val purchaseRepository: PurchaseRepository,
    private val invoiceRepository: InvoiceRepository,
    private val creditCardRepository: CreditCardRepository,
    private val creditCardService: CreditCardService
) {
    fun createPurchase(purchase: PurchaseDTO, purchaseDate: LocalDate): PurchaseEntity {
        val newPurchase = purchaseRepository.createPurchase(purchase.value, purchaseDate, purchase.description)
        this.addToCurrentInvoice(newPurchase, purchase.creditCard)
        return newPurchase
    }

    fun createPurchases(purchase: PurchaseDTO, purchaseDate: LocalDate): MutableList<PurchaseEntity> {
        val purchases = mutableListOf<PurchaseEntity>()
        val installmentValue = purchase.value / purchase.installments

        for (i in 0 until purchase.installments) {
            val installmentDate = purchaseDate.plusMonths(i.toLong())
            val newPurchase = purchaseRepository.createPurchase(installmentValue, installmentDate, "${purchase.description} - Parcela ${i + 1}/${purchase.installments}")
            this.addToInvoice(newPurchase, purchase.creditCard, installmentDate)
            purchases.add(newPurchase)
        }

        return purchases
    }

    private fun addToCurrentInvoice(purchase: PurchaseEntity, creditCardId: Int) {
        val creditCard = creditCardRepository.getCreditCardById(creditCardId)
            ?: throw IllegalArgumentException("Credit card not found")
        invoiceRepository.addPurchaseToCurrentInvoice(purchase, creditCard)
        try {
            creditCardService.updateCreditCardAvailableLimit(creditCardId, creditCard.limiteDisponivel - purchase.value)
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Limite disponível insuficiente")
        }
    }

    private fun addToInvoice(purchase: PurchaseEntity, creditCardId: Int, invoiceDate: LocalDate) {
        val creditCard = creditCardRepository.getCreditCardById(creditCardId)
            ?: throw IllegalArgumentException("Credit card not found")
        invoiceRepository.addPurchaseToInvoice(purchase, creditCard, invoiceDate)
        creditCardService.updateCreditCardAvailableLimit(creditCardId, creditCard.limiteDisponivel - purchase.value)
    }
}
