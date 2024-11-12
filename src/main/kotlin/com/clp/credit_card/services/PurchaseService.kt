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
    /**
     * Creates a new purchase and adds it to the current invoice.
     *
     * @param purchase The purchase details.
     * @param purchaseDate The date of the purchase.
     * @return The created PurchaseEntity.
     */
    fun createPurchase(purchase: PurchaseDTO, purchaseDate: LocalDate): PurchaseEntity {
        val newPurchase = purchaseRepository.createPurchase(purchase.value, purchaseDate, purchase.description)
        this.addToCurrentInvoice(newPurchase, purchase.creditCard)
        return newPurchase
    }

    /**
     * Creates multiple purchases based on the number of installments.
     *
     * @param purchase The purchase details.
     * @param purchaseDate The date of the purchase.
     * @return A list of created PurchaseEntity objects.
     */
    fun createPurchases(purchase: PurchaseDTO, purchaseDate: LocalDate): MutableList<PurchaseEntity> {
        val purchases = mutableListOf<PurchaseEntity>()
        val installmentValue = purchase.value / purchase.installments

        for (i in 0 until purchase.installments) {
            val installmentDate = purchaseDate.plusMonths(i.toLong())
            val newPurchase = purchaseRepository.createPurchase(
                installmentValue,
                installmentDate,
                "${purchase.description} - Parcela ${i + 1}/${purchase.installments}"
            )
            this.addToInvoice(newPurchase, purchase.creditCard, installmentDate)
            purchases.add(newPurchase)
        }

        return purchases
    }

    /**
     * Adds a purchase to the current invoice and updates the credit card's available limit.
     *
     * @param purchase The purchase entity to be added.
     * @param creditCardId The ID of the credit card used for the purchase.
     * @throws IllegalArgumentException if the credit card is not found or if the available limit is insufficient.
     */
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

    /**
     * Adds a purchase to a specific invoice and updates the credit card's available limit.
     *
     * @param purchase The purchase entity to be added.
     * @param creditCardId The ID of the credit card used for the purchase.
     * @param invoiceDate The date of the invoice to which the purchase will be added.
     * @throws IllegalArgumentException if the credit card is not found.
     */
    private fun addToInvoice(purchase: PurchaseEntity, creditCardId: Int, invoiceDate: LocalDate) {
        val creditCard = creditCardRepository.getCreditCardById(creditCardId)
            ?: throw IllegalArgumentException("Credit card not found")
        invoiceRepository.addPurchaseToInvoice(purchase, creditCard, invoiceDate)
        creditCardService.updateCreditCardAvailableLimit(creditCardId, creditCard.limiteDisponivel - purchase.value)
    }

    /**
     * Deletes all purchases associated with a specific invoice.
     *
     * @param invoiceId The ID of the invoice whose purchases are to be deleted.
     */
    fun deleteAllPurchasesByInvoice(invoiceId: Int) {
        purchaseRepository.deleteAllPurchasesByInvoice(invoiceId)
    }
}
