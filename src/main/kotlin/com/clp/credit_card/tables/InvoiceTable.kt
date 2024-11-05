package com.clp.credit_card.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.date

object InvoiceTable : IntIdTable() {
    val value = double("value")
    val dueDate = date("due_date")
    val paymentDate = date("payment_date")
    val purchases = reference("purchases", PurchaseTable)
}