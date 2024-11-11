package com.clp.credit_card.controllers

import com.clp.credit_card.models.dto.InvoiceResponseWrapper
import com.clp.credit_card.models.dto.PurchaseResponseWrapper
import com.clp.credit_card.services.CreditCardService
import com.clp.credit_card.services.InvoiceService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate
import java.time.Month
import java.time.Year

@RestController
@RequestMapping("/api/invoices")
class InvoiceController(private val invoiceService: InvoiceService) {
    @GetMapping
    fun getAllInvoices(@PathVariable cardId: Int, @PathVariable withPurchases: Boolean): ResponseEntity<List<InvoiceResponseWrapper>> {
        return try {
            val invoices : List<InvoiceResponseWrapper>;
            if (withPurchases) {
                invoices = invoiceService.getAllInvoicesWithPurchases(cardId)
            } else {
                invoices = invoiceService.getAllInvoices(cardId)
            }
            ResponseEntity.ok(invoices)
        } catch (e: IllegalArgumentException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
        }
    }

    @GetMapping
    fun getInvoice (@PathVariable cardId: Int, @PathVariable month:Month, @PathVariable year: Year, @PathVariable withPurchases: Boolean): ResponseEntity<InvoiceResponseWrapper> {
        return try {
            val invoices : InvoiceResponseWrapper;
            if (withPurchases) {
                invoices = invoiceService.getInvoiceWithPurchases(cardId, month, year)
            } else {
                invoices = invoiceService.getInvoice(cardId, month, year)
            }
            ResponseEntity.ok(invoices)
        } catch (e: IllegalArgumentException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
        }
    }

    @GetMapping("/pay/{id}")
    fun payInvoice (@PathVariable id: String) : ResponseEntity<String>{
        val barCode = (1..12).map { (0..9).random() }.joinToString("")
        return ResponseEntity.ok(barCode)
    }

}