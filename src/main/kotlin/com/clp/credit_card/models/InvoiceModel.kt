package com.clp.credit_card.models

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date

object InvoiceModel : Table("Invoice") {
    val id = integer("id").autoIncrement()
    val value = double("value")
    val dataDeVenciemento = date("data_de_vencimento")
    val dataDePagamento = date("data_de_pagamento")
}