package com.clp.credit_card.models
import java.time.LocalDate

enum class StatusEnum {
    Ativo,
    Bloqueado,
    Cancelado,
}

data class CreditCard(
    var id: Int = 0,
    val numeroCartao: String,
    val cvv: String,
    val dataValidade: LocalDate,
    val limiteDisponivel: Double,
    val status: StatusEnum,
    val limiteTotal: Double,
    val idUsuario: Int,
    val idFatura: Int?
)

