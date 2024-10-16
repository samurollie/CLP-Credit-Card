package com.clp.models
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class CreditCardRepository {

    fun addCreditCard(card: CreditCard): CreditCard {
        transaction {
            CreditCards.insert {
                it[numeroCartao] = card.numeroCartao
                it[cvv] = card.cvv
                it[dataValidade] = card.dataValidade
                it[limiteDisponivel] = card.limiteDisponivel
                it[status] = card.status
                it[limiteTotal] = card.limiteTotal
                it[idUsuario] = card.idUsuario
                it[idFatura] = card.idFatura
            }
        }
        return card
    }

    fun getAllCreditCards(): List<CreditCard> {
        return transaction {
            CreditCards.selectAll().map { toCreditCard(it) }
        }
    }

    fun getCreditCardById(id: Int): CreditCard? {
        return transaction {
            CreditCards.selectAll().where { CreditCards.id eq id }
                .map { toCreditCard(it) }
                .singleOrNull()
        }
    }

    fun updateCreditCard(id: Int, updatedCard: CreditCard): Boolean {
        return transaction {
            CreditCards.update({ CreditCards.id eq id }) {
                it[numeroCartao] = updatedCard.numeroCartao
                it[cvv] = updatedCard.cvv
                it[dataValidade] = updatedCard.dataValidade
                it[limiteDisponivel] = updatedCard.limiteDisponivel
                it[status] = updatedCard.status
                it[limiteTotal] = updatedCard.limiteTotal
                it[idUsuario] = updatedCard.idUsuario
                it[idFatura] = updatedCard.idFatura
            } > 0
        }
    }

    fun deleteCreditCard(id: Int): Boolean {
        return transaction {
            CreditCards.deleteWhere { CreditCards.id eq id } > 0
        }
    }

    // Helper function to map ResultRow to CreditCard
    private fun toCreditCard(row: ResultRow): CreditCard {
        return CreditCard(
            id = row[CreditCards.id].value,
            numeroCartao = row[CreditCards.numeroCartao],
            cvv = row[CreditCards.cvv],
            dataValidade = row[CreditCards.dataValidade],
            limiteDisponivel = row[CreditCards.limiteDisponivel],
            status = row[CreditCards.status],
            limiteTotal = row[CreditCards.limiteTotal],
            idUsuario = row[CreditCards.idUsuario],
            idFatura = row[CreditCards.idFatura]
        )
    }
}
