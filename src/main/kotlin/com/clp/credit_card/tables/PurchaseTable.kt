package com.clp.credit_card.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.date

object PurchaseTable : IntIdTable() {
    val value = double("value")
    val description = varchar("description", 255)
    val data = date("data")
    val disputed = bool("disputed").default(false).nullable()
    val invoiceId = reference("invoice_id", InvoiceTable)
}