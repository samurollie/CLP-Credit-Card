package com.clp.credit_card.controllers

import com.clp.credit_card.controllers.PurchaseController.PurchaseResponse
import com.clp.credit_card.services.InvoiceService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/api/invoices")
class InvoiceController(private val invoiceService: InvoiceService) {
    data class InvoiceResponse(
        val value: Double,
        val dueDate: LocalDate,
        val closingDate: LocalDate,
        val isPaid: Boolean,
        val paymentDate: LocalDate,
        val cardId: Int
    )
    sealed class InvoiceResponseWrapper {
        data class Single(val invoice: InvoiceResponse) : InvoiceResponseWrapper()
        data class Multiple(val invoice: InvoiceResponse) : InvoiceResponseWrapper()
    }

    @GetMapping
    fun getAllInvoices(@PathVariable cardId: Int, @PathVariable withPurchases: Boolean): ResponseEntity<String> {
        return ResponseEntity.ok(":D")
    }

    @PostMapping
    fun createInvoice(@RequestBody invoice: String): ResponseEntity<String> {
//        val createdInvoice = invoiceService.createInvoice(invoice)
        return ResponseEntity.ok("createdInvoice")
    }
}