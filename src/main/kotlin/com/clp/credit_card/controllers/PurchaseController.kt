package com.clp.credit_card.controllers

import com.clp.credit_card.models.dto.PurchaseDTO
import com.clp.credit_card.models.dto.PurchaseResponse
import com.clp.credit_card.models.dto.PurchaseResponseWrapper
import com.clp.credit_card.services.PurchaseService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate

@RestController
@RequestMapping("/api/purchase")
class PurchaseController(private val purchaseService: PurchaseService) {


    @PostMapping
    fun addPurchase(@RequestBody purchase: PurchaseDTO): ResponseEntity<PurchaseResponseWrapper> {
        println(purchase)

        return try {
            val today = LocalDate.now()
            val response = if (purchase.installments > 1) {
                println("Creating multiple purchases")
                val purchases = purchaseService.createPurchases(purchase, today)
                PurchaseResponseWrapper.Multiple(purchases.map {
                    PurchaseResponse(it.id.value, it.value, it.date, it.description)
                })
            } else {
                println("Creating single purchase")
                val singlePurchase = purchaseService.createPurchaseAndAddToInvoice(purchase, today)
                PurchaseResponseWrapper.Single(PurchaseResponse(singlePurchase.id.value, singlePurchase.value, singlePurchase.date, singlePurchase.description))
            }
            ResponseEntity.ok(response)
        } catch (e: IllegalArgumentException) {
            println(e)
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
        }
    }
}