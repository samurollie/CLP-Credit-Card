package com.clp.credit_card.controllers

import com.clp.credit_card.models.dto.PurchaseDTO
import com.clp.credit_card.services.PurchaseService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/api/purchase")
class PurchaseController(private val purchaseService: PurchaseService) {
    data class PurchaseResponse(val id: Int, val value: Double, val date: LocalDate, val description: String)
    sealed class PurchaseResponseWrapper {
        data class Single(val purchase: PurchaseResponse) : PurchaseResponseWrapper()
        data class Multiple(val purchases: List<PurchaseResponse>) : PurchaseResponseWrapper()
    }

    @PostMapping
    fun addPurchase(@RequestBody purchase: PurchaseDTO): ResponseEntity<PurchaseResponseWrapper> {
        return try {
            val today = LocalDate.now()
            println(purchase)
            val response = if (purchase.installments > 0) {
                println("Creating multiple purchases")
                val purchases = purchaseService.createPurchases(purchase, today)
                PurchaseResponseWrapper.Multiple(purchases.map {
                    PurchaseResponse(it.id.value, it.value, it.data, it.description)
                })
            } else {
                println("Creating single purchase")
                val singlePurchase = purchaseService.createPurchaseAndAddToInvoice(purchase, today)
                PurchaseResponseWrapper.Single(PurchaseResponse(singlePurchase.id.value, singlePurchase.value, singlePurchase.data, singlePurchase.description))
            }
            ResponseEntity.ok(response)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)
        }
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.message)
    }
}