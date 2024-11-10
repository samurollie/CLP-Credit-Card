package com.clp.credit_card.entities

import com.clp.credit_card.tables.CreditCardTable
import com.clp.credit_card.tables.InvoiceTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class CreditCardEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object: IntEntityClass<CreditCardEntity>(CreditCardTable)

    var numeroCartao by CreditCardTable.numeroCartao
    var cvv by CreditCardTable.cvv
    var dataValidade by CreditCardTable.dataValidade
    var limiteDisponivel by CreditCardTable.limiteDisponivel
    var status by CreditCardTable.status
    var limiteTotal by CreditCardTable.limiteTotal
    var idUsuario by CreditCardTable.idUsuario
    var closingDay by CreditCardTable.closingDay
    val fatura by InvoiceEntity referrersOn InvoiceTable.cardId // Refere-se a todas as faturas desse cartão
}