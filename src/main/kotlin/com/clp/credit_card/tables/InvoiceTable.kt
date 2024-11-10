package com.clp.credit_card.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.date

object
InvoiceTable : IntIdTable() {
    val value = double("value")
    val dueDate = date("due_date") // Data de Pagamento
    val closingDate = date("closing_date") // Data de Fechamento
    val isPaid = bool("is_paid").default(false) // Se já foi paga
    val paymentDate = date("payment_date").nullable() // Data em que foi paga
    val cardId = reference("card_id", CreditCardTable)
}