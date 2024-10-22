package com.clp.models
import com.clp.controllers.CreditCardController
import com.clp.repository.CreditCardRepository
import com.clp.repository.UserRepository
import com.clp.services.CreditCardServices
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate

fun main() {
/*
    // Connect to the database
    Database.connect("jdbc:postgresql://localhost:5432/cartao_de_credito_db", driver = "org.postgresql.Driver",
        user = "postgres", password = "adminadmin")

    // Create an instance of CreditCardRepository
    val creditCardRepository = CreditCardRepository()
    val userRepository = UserRepository()

    val controller =  CreditCardController();
    val newUserId = userRepository.addUser()
    controller.createCreditCard(newUserId)*/
/*
    // Test the CRUD operations
    transaction {
        creditCardRepository.deleteAllCreditCards()
        userRepository.deleteAllUsers()

        val newUserId = userRepository.addUser() // Calls the addUser function
        println("New user added with ID: $newUserId")

        // Create a new credit card
        val newCard = CreditCardServices.createCreditCard(newUserId)

        // Read all credit cards
        val allCards = creditCardRepository.getAllCreditCards()
        println("All Credit Cards: $allCards")

        // Read a specific credit card by ID (assuming the new ID is 1)
        val cardIdToRead = newCard.id
        val cardRead = creditCardRepository.getCreditCardById(cardIdToRead)
        println("Read Credit Card with ID $cardIdToRead: $cardRead")

        // Update the credit card
        val updatedCard = newCard.copy(limiteDisponivel = 1500.0)
        creditCardRepository.updateCreditCard(cardIdToRead, updatedCard)
        println("Updated Credit Card: $updatedCard")

        userRepository.deleteUser(newUserId);
        println("Deleted User with ID $newUserId")
        // Delete the credit card
        creditCardRepository.deleteCreditCard(cardIdToRead)
    }
*/
}
