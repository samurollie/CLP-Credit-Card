package com.clp.credit_card.models

import java.util.*

data class PurchaseModel(
    val data: Date,
    val valor: Double,
    val loja: String
)
