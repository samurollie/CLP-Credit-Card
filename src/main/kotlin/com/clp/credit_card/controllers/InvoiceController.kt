package com.clp.credit_card.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/invoices")
class InvoiceController {

    @GetMapping
    fun getInvoices(): ResponseEntity<String> {
        return ResponseEntity.ok(":D")
    }
}