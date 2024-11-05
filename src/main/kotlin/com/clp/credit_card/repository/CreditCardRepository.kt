package com.clp.credit_card.repository
import com.clp.credit_card.models.CreditCard
import com.clp.credit_card.models.CreditCards
import com.clp.credit_card.models.StatusEnum
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository
import java.time.LocalDate
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

    fun updateCreditLimit(id: Int, newLimit: Double) {
        transaction {
            CreditCards.update({ CreditCards.id eq id }) {
                it[limiteTotal] = newLimit
            }
        }
    }

    fun updateCreditStatus(id: Int, status: StatusEnum) {
        transaction {
            CreditCards.update({ CreditCards.id eq id }) {
                it[CreditCards.status] = status
            }
        }
    }

    fun updateCreditExpirationDate(id: Int, dataValidade: LocalDate) {
        transaction {
            CreditCards.update({ CreditCards.id eq id }) {
                it[CreditCards.dataValidade] = dataValidade
            }
        }
    }


    fun deleteCreditCardById(id: Int): Boolean {
        return transaction {
            val deletedCardCount = CreditCards.deleteWhere { CreditCards.id eq id}
            if (deletedCardCount > 0) {
                println("Credit card with number $id deleted.")
            } else {
                println("Credit card with number $id not found.")
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
