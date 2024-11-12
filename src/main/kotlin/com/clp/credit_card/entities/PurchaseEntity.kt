package com.clp.credit_card.entities

import com.clp.credit_card.tables.PurchaseTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

/**
 * Entity class representing a purchase.
 *
 * @property id The unique identifier of the purchase.
 */
class PurchaseEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PurchaseEntity>(PurchaseTable)

    /**
     * The date of the purchase.
     */
    var date by PurchaseTable.data

    /**
     * The value of the purchase.
     */
    var value by PurchaseTable.value

    /**
     * The description of the purchase.
     */
    var description by PurchaseTable.description

    /**
     * Indicates if the purchase is disputed.
     */
    var disputed by PurchaseTable.disputed

    /**
     * The invoice to which this purchase belongs.
     */
    var invoice by InvoiceEntity referencedOn PurchaseTable.invoiceId
}