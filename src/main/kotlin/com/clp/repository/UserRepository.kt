package com.clp.repository
import com.clp.models.CreditCard
import com.clp.models.CreditCards
import com.clp.models.User
import com.clp.models.UsuariosTemp
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

@Repository
public class UserRepository {

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
            val deletedCreditCardsCount = CreditCards.deleteWhere { CreditCards.idUsuario eq id }
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
            val deletedCreditCardsCount = CreditCards.deleteAll()
            println("$deletedCreditCardsCount credit card(s) deleted.")

            // Now delete all users
            val deletedUsersCount = UsuariosTemp.deleteAll()
            println("$deletedUsersCount user(s) deleted.")
        }
    }
}