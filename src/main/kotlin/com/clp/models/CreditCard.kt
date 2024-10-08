package com.clp.models
import com.clp.models.CreditCardManager.Companion.calculateLimite
import com.clp.models.CreditCardManager.Companion.generateCardCVV
import com.clp.models.CreditCardManager.Companion.generateCardNumber
import java.time.LocalDate

class CreditCard(
    private val dataDeVencimento: LocalDate,
    private val limite: LocalDate,
    private val codigoVerificador: String,
    private val numeroCartao: String,
    private val usuario: String,
) {
    fun CreditCard(){
        val connection = DatabaseManager.connectToDatabase()

        connection?.let {
            DatabaseManager.createLine(it)
            DatabaseManager.closeConnection(it)
        }
    }

    public fun getDataDeVencimento(): LocalDate {
        return this.dataDeVencimento
    }

    public fun getLimite() : LocalDate {
        return this.limite
    }

    public fun getCodigoVerificador() : String {
        return this.codigoVerificador
    }

    public fun getNumeroCartao() : String {
        return this.numeroCartao
    }

    public fun getUsuario() : String {
        return this.usuario
    }
}
