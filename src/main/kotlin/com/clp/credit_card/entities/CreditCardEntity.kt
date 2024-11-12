package com.clp.credit_card.entities

import com.clp.credit_card.tables.CreditCardTable
import com.clp.credit_card.tables.InvoiceTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

/**
 * Entity class representing a credit card.
 *
 * @property id The unique identifier of the credit card.
 */
class CreditCardEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CreditCardEntity>(CreditCardTable)

    /** The credit card number. */
    var numeroCartao by CreditCardTable.numeroCartao

    /** The CVV code of the credit card. */
    var cvv by CreditCardTable.cvv

    /** The expiration date of the credit card. */
    var dataValidade by CreditCardTable.dataValidade

    /** The available limit of the credit card. */
    var limiteDisponivel by CreditCardTable.limiteDisponivel

    /** The status of the credit card. */
    var status by CreditCardTable.status

    /** The total limit of the credit card. */
    var limiteTotal by CreditCardTable.limiteTotal

    /** The user ID associated with the credit card. */
    var idUsuario by CreditCardTable.idUsuario

    /** The closing day of the credit card. */
    var closingDay by CreditCardTable.closingDay

    /** Refers to all invoices associated with this credit card. */
    val fatura by InvoiceEntity referrersOn InvoiceTable.cardId
}