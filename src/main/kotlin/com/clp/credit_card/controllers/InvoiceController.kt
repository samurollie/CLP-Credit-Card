package com.clp.credit_card.controllers

import com.clp.credit_card.models.dto.InvoiceResponseWrapper
import com.clp.credit_card.services.InvoiceService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate
import java.time.Year

@RestController
@RequestMapping("/api/invoices")
class InvoiceController(private val invoiceService: InvoiceService) {
    /**
     * Retrieves all invoices for a given credit card.
     *
     * @param cardId The ID of the credit card.
     * @param withPurchases Flag indicating whether to include purchases in the response.
     * @return A ResponseEntity containing a list of InvoiceResponseWrapper objects.
     */
    @GetMapping("/all")
    fun getAllInvoices(
        @RequestParam cardId: Int,
        @RequestParam withPurchases: Boolean
    ): ResponseEntity<List<InvoiceResponseWrapper>> {
        return try {
            val invoices: List<InvoiceResponseWrapper>
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

    /**
 * Retrieves a specific invoice for a given credit card and date.
 *
 * @param cardId The ID of the credit card.
 * @param month The month of the invoice.
 * @param year The year of the invoice.
 * @param withPurchases Flag indicating whether to include purchases in the response.
 * @return A ResponseEntity containing an InvoiceResponseWrapper object.
 */
@GetMapping
fun getInvoice(
        @RequestParam cardId: Int,
        @RequestParam month: String,
        @RequestParam year: String,
        @RequestParam withPurchases: Boolean
    ): ResponseEntity<InvoiceResponseWrapper> {
        return try {
            val invoices: InvoiceResponseWrapper
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

/**
 * Generates a barcode for the specified invoice ID.
 *
 * @param id The ID of the invoice.
 * @return A ResponseEntity containing the generated barcode as a string.
 */
@GetMapping("/pay/{id}")
fun getBarCode(@PathVariable id: String): ResponseEntity<String> {
        val barCode = (1..48).map { (0..9).random() }.joinToString("")
        return ResponseEntity.ok(barCode)
    }

/**
 * Generates a barcode for the current invoice.
 *
 * @return A ResponseEntity containing the generated barcode as a string.
 */
@GetMapping("/pay")
fun getCurrentBarCode(): ResponseEntity<String> {
        val barCode = (1..48).map { (0..9).random() }.joinToString("")
        return ResponseEntity.ok(barCode)
    }

    @PostMapping("/pay/{id}")
    fun payInvoice(@PathVariable id: Int, @RequestParam cardId: Int): ResponseEntity<String> {
        return try {
            invoiceService.payInvoice(id, cardId)
            ResponseEntity.ok("Invoice paid successfully")
        } catch (e: IllegalArgumentException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
        }
    }

/**
 * Pays the current invoice for a given credit card.
 *
 * @param cardId The ID of the credit card.
 * @return A ResponseEntity containing a success message.
 */
@PostMapping("/pay")
fun payCurrentInvoice(@RequestParam cardId: Int): ResponseEntity<String> {
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