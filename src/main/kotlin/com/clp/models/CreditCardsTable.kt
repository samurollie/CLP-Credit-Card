package com.clp.models
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.date

object CreditCards : IntIdTable("cartao_de_credito") {
    val numeroCartao: Column<String> = varchar("numero_cartao", 255).uniqueIndex()
    val cvv: Column<String> = varchar("cvv", 3)  // Adjust length if needed
    val dataValidade: Column<java.time.LocalDate> = date("data_validade")
    val limiteDisponivel: Column<Double> = double("limite_disponivel")
    val status: Column<StatusEnum> = enumeration("status", StatusEnum::class)
    val limiteTotal: Column<Double> = double("limite_total")
    val idUsuario: Column<Int> = integer("id_usuario").uniqueIndex()
    val idFatura: Column<Int?> = integer("id_fatura").nullable() // Nullable
}

