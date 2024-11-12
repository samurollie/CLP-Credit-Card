package com.clp.credit_card.repository

import com.clp.credit_card.models.CreditCard
import com.clp.credit_card.models.StatusEnum
import com.clp.credit_card.tables.CreditCardTable
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

    /**
     * Adds a new credit card to the database.
     *
     * @param card The credit card to be added.
     * @return The added credit card with the generated ID.
     */
    fun addCreditCard(card: CreditCard): CreditCard {
        transaction {
            // Use insertAndGetId to insert and get the generated ID
            val generatedId = CreditCardTable.insertAndGetId {
                it[numeroCartao] = card.numeroCartao
                it[cvv] = card.cvv
                it[dataValidade] = card.dataValidade
                it[limiteDisponivel] = card.limiteDisponivel
                it[status] = card.status
                it[limiteTotal] = card.limiteTotal
                it[idUsuario] = card.idUsuario
                it[closingDay] = card.closingDay
            }
            // Assign the generated ID to the card's id property
            card.id = generatedId.value // Assuming 'id' is of type Long in CreditCard
        }
        return card
    }

    /**
     * Retrieves all credit cards from the database.
     *
     * @return A list of all credit cards.
     */
    fun getAllCreditCards(): List<CreditCard> {
        return transaction {
            CreditCardTable.selectAll().map { toCreditCard(it) }
        }
    }

    /**
     * Retrieves a credit card by its ID.
     *
     * @param id The ID of the credit card to retrieve.
     * @return The credit card with the specified ID, or null if not found.
     */
    fun getCreditCardById(id: Int): CreditCard? {
        return transaction {
            CreditCardTable.selectAll().where { CreditCardTable.id eq id }
                .map { toCreditCard(it) }
                .singleOrNull()
        }
    }

    /**
     * Updates the total credit limit of a credit card.
     *
     * @param id The ID of the credit card to update.
     * @param newLimit The new total credit limit to set.
     */
    fun updateCreditLimit(id: Int, newLimit: Double) {
        transaction {
            val novoDisponivel = newLimit + CreditCardTable.selectAll().where { CreditCardTable.id eq id }
                .single()[CreditCardTable.limiteDisponivel]

            CreditCardTable.update({ CreditCardTable.id eq id }) {
                it[limiteTotal] = newLimit
                it[limiteDisponivel] = novoDisponivel
            }
        }
    }

    /**
     * Updates the available credit limit of a credit card.
     *
     * @param id The ID of the credit card to update.
     * @param newLimit The new available credit limit to set.
     */
    fun updateAvailableLimit(id: Int, newLimit: Double) {
        transaction {
            CreditCardTable.update({ CreditCardTable.id eq id }) {
                it[limiteDisponivel] = newLimit
            }
        }
    }

    /**
     * Updates the status of a credit card.
     *
     * @param id The ID of the credit card to update.
     * @param status The new status to set.
     */
    fun updateCreditStatus(id: Int, status: StatusEnum) {
        transaction {
            CreditCardTable.update({ CreditCardTable.id eq id }) {
                it[CreditCardTable.status] = status
            }
        }
    }

    /**
     * Updates the expiration date of a credit card.
     *
     * @param id The ID of the credit card to update.
     * @param dataValidade The new expiration date to set.
     */
    fun updateCreditExpirationDate(id: Int, dataValidade: LocalDate) {
        transaction {
            CreditCardTable.update({ CreditCardTable.id eq id }) {
                it[CreditCardTable.dataValidade] = dataValidade
            }
        }
    }


    /**
     * Deletes a credit card by its ID.
     *
     * @param id The ID of the credit card to delete.
     * @return True if the credit card was deleted, false otherwise.
     */
    fun deleteCreditCardById(id: Int): Boolean {
        return transaction {
            val deletedCardCount = CreditCardTable.deleteWhere { CreditCardTable.id eq id }
            if (deletedCardCount > 0) {
                println("Credit card with number $id deleted.")
            } else {
                println("Credit card with number $id not found.")
            }
            deletedCardCount > 0 // Return true if the card was deleted
        }
    }

    /**
     * Deletes all credit cards from the database.
     */
    fun deleteAllCreditCards() {
        transaction {
            // Deletes all entries from the CreditCards table
            val deletedCount = CreditCardTable.deleteAll()
            println("$deletedCount credit card(s) deleted.")
        }
    }

    fun existsCreditCardNumber(creditCardNumber: String): Boolean {
        return transaction {
            CreditCardTable.selectAll().where { CreditCardTable.numeroCartao eq creditCardNumber }
                .singleOrNull() != null
        }
    }

    // Helper function to map ResultRow to CreditCard
    private fun toCreditCard(row: ResultRow): CreditCard {
        return CreditCard(
            id = row[CreditCardTable.id].value,
            numeroCartao = row[CreditCardTable.numeroCartao],
            cvv = row[CreditCardTable.cvv],
            dataValidade = row[CreditCardTable.dataValidade],
            limiteDisponivel = row[CreditCardTable.limiteDisponivel],
            status = row[CreditCardTable.status],
            limiteTotal = row[CreditCardTable.limiteTotal],
            idUsuario = row[CreditCardTable.idUsuario],
            closingDay = row[CreditCardTable.closingDay]
        )
    }
}

