package com.clp.credit_card.controllers

import com.clp.credit_card.services.InvoiceService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/invoices")
class InvoiceController(private val invoiceService: InvoiceService) {

    @GetMapping
    fun getAllInvoices(): ResponseEntity<String> {
        return ResponseEntity.ok(":D")
    }

    @PostMapping
    fun createInvoice(@RequestBody invoice: String): ResponseEntity<String> {
//        val createdInvoice = invoiceService.createInvoice(invoice)
        return ResponseEntity.ok("createdInvoice")
    }
}