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
    @GetMapping("/all")
    fun getAllInvoices(@RequestParam cardId: Int, @RequestParam withPurchases: Boolean): ResponseEntity<List<InvoiceResponseWrapper>> {
        return try {
            val invoices : List<InvoiceResponseWrapper>;
            if (withPurchases) {
                invoices = invoiceService.getAllInvoicesWithPurchases(cardId)
            } else {
                invoices = invoiceService.getAllInvoices(cardId)
                println(invoices)
            }
            ResponseEntity.ok(invoices)
        } catch (e: IllegalArgumentException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
        }
    }

    @GetMapping
    fun getInvoice (@RequestParam cardId: Int, @RequestParam month:String, @RequestParam year: String, @RequestParam withPurchases: Boolean): ResponseEntity<InvoiceResponseWrapper> {
        return try {
            val invoices : InvoiceResponseWrapper;
            val tempDate = LocalDate.of(year.toInt(), month.toInt(), 1)
            val monthAsDate = tempDate.month
            val yearAsDate = Year.of(tempDate.year)
            if (withPurchases) {
                invoices = invoiceService.getInvoiceWithPurchases(cardId, monthAsDate, yearAsDate)
            } else {
                invoices = invoiceService.getInvoice(cardId, monthAsDate, yearAsDate)
            }
            ResponseEntity.ok(invoices)
        } catch (e: IllegalArgumentException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
        }
    }

    @GetMapping("/pay/{id}")
    fun getBarCode (@PathVariable id: String) : ResponseEntity<String>{
        val barCode = (1..48).map { (0..9).random() }.joinToString("")
        return ResponseEntity.ok(barCode)
    }

    @GetMapping("/pay")
    fun getCurrentBarCode() : ResponseEntity<String> {
        val barCode = (1..48).map { (0..9).random() }.joinToString("")
        return ResponseEntity.ok(barCode)
    }

    @PostMapping("/pay/{id}")
    fun payInvoice (@PathVariable id: Int, @RequestParam cardId: Int) : ResponseEntity<String>{
        return try {
            invoiceService.payInvoice(id, cardId)
            ResponseEntity.ok("Invoice paid successfully")
        } catch (e: IllegalArgumentException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
        }
    }

    @PostMapping("/pay")
    fun payCurrentInvoice (@RequestParam cardId: Int) : ResponseEntity<String>{
        return try {
            invoiceService.payCurrentInvoice(cardId)
            ResponseEntity.ok("Invoice paid successfully")
        } catch (e: IllegalArgumentException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
        }
    }
}