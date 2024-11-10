package com.clp.credit_card.entities

import com.clp.credit_card.tables.PurchaseTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class PurchaseEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object: IntEntityClass<PurchaseEntity>(PurchaseTable)

    var data by PurchaseTable.data
    var value by PurchaseTable.value
    var description by PurchaseTable.description
    var disputed by PurchaseTable.disputed
    var invoice by InvoiceEntity referencedOn PurchaseTable.invoiceId // Refere-se a fatura a qual essa compra pertence
}