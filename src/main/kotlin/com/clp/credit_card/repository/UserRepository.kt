package com.clp.credit_card.repository

import com.clp.credit_card.tables.CreditCardTable
import com.clp.credit_card.tables.UsuariosTemp
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository
import javax.sql.DataSource

@Repository
class UserRepository(private val dataSource: DataSource) {

    init {
        // Initialize Exposed with Spring's DataSource
        Database.connect(dataSource)
    }

    fun addUser(): Int {
        var newUserId: Int = 0
        transaction {
            newUserId = UsuariosTemp.insertAndGetId {
                // No additional fields to set
            }.value
        }
        return newUserId // Return the generated id
    }

    fun deleteUser(id: Int): Boolean {
        return transaction {
            // First delete all associated credit cards
            val deletedCreditCardsCount = CreditCardTable.deleteWhere { idUsuario eq id }
            if (deletedCreditCardsCount > 0) {
                println("$deletedCreditCardsCount credit card(s) deleted for user ID $id.")
            }

            // Now delete the user
            val deletedUserCount = UsuariosTemp.deleteWhere { UsuariosTemp.id eq id }
            if (deletedUserCount > 0) {
                println("User with ID $id deleted.")
            } else {
                println("User with ID $id not found.")
            }

            deletedUserCount > 0 // Return true if user was deleted
        }
    }

    fun deleteAllUsers() {
        return transaction {
            // First delete all associated credit cards
            val deletedCreditCardsCount = CreditCardTable.deleteAll()
            println("$deletedCreditCardsCount credit card(s) deleted.")

            // Now delete all users
            val deletedUsersCount = UsuariosTemp.deleteAll()
            println("$deletedUsersCount user(s) deleted.")
        }
    }
}