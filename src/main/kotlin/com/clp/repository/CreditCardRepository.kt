package com.clp.repository
import com.clp.models.CreditCard
import com.clp.models.CreditCards
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository
import javax.sql.DataSource

@Repository
class CreditCardRepository(private val dataSource: DataSource) {

    init {
        // Initialize Exposed with Spring's DataSource
        Database.connect(dataSource)
    }

    fun addCreditCard(card: CreditCard): CreditCard {
        transaction {
            // Use insertAndGetId to insert and get the generated ID
            val generatedId = CreditCards.insertAndGetId {
                it[numeroCartao] = card.numeroCartao
                it[cvv] = card.cvv
                it[dataValidade] = card.dataValidade
                it[limiteDisponivel] = card.limiteDisponivel
                it[status] = card.status
                it[limiteTotal] = card.limiteTotal
                it[idUsuario] = card.idUsuario
                it[idFatura] = card.idFatura
            }
            // Assign the generated ID to the card's id property
            card.id = generatedId.value // Assuming 'id' is of type Long in CreditCard
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
            val deletedCardCount = CreditCards.deleteWhere { CreditCards.id eq id }
            if (deletedCardCount > 0) {
                println("Credit card with ID $id deleted.")
            } else {
                println("Credit card with ID $id not found.")
            }
            deletedCardCount > 0 // Return true if the card was deleted
        }
    }

    fun deleteAllCreditCards() {
        transaction {
            // Deletes all entries from the CreditCards table
            val deletedCount = CreditCards.deleteAll()
            println("$deletedCount credit card(s) deleted.")
        }
    }

    fun existsCreditCardNumber(creditCardNumber: String): Boolean {
        return transaction {
            CreditCards.selectAll().where { CreditCards.numeroCartao eq creditCardNumber }
                .singleOrNull() != null
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
