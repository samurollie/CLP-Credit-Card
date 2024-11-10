package com.clp.credit_card.controllers

import com.clp.credit_card.entities.PurchaseEntity
import com.clp.credit_card.services.PurchaseService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/api/purchase")
class PurchaseController(private val purchaseService: PurchaseService) {
    data class PurchaseRequest(val value: Double, val description: String)
    data class PurchaseResponse(val id: Int, val value: Double, val date: LocalDate, val description: String)

    @PostMapping
    fun addPurchase(@RequestBody purchaseRequest: PurchaseRequest): ResponseEntity<PurchaseResponse> {
        return try {
            val today = LocalDate.now()
            println(purchaseRequest)
            val purchase = purchaseService.createPurchaseAndAddToInvoice(purchaseRequest.value, today, purchaseRequest.description)
            val response = PurchaseResponse(purchase.id.value, purchase.value, purchase.data, purchase.description)
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