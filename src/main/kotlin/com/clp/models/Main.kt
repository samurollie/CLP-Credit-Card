// src/main/kotlin/com/example/project/Main.kt

package com.clp.models
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate

fun main() {
    // Connect to the database
    Database.connect("jdbc:postgresql://localhost:5432/GerenciadorDeCartao", driver = "org.postgresql.Driver",
        user = "postgres", password = "adminadmin")

    // Create an instance of CreditCardRepository
    val creditCardRepository = CreditCardRepository()

    // Test the CRUD operations
    transaction {
        // Create a new credit card
        val newCard = CreditCard(
            id = 0, // Let the database auto-generate this
            numeroCartao = "1234567812345678",
            cvv = "123",
            dataValidade = LocalDate.of(2025, 12, 31),
            limiteDisponivel = 1000.0,
            status = StatusEnum.Ativo,
            limiteTotal = 2000.0,
            idUsuario = 1,
            idFatura = null
        )
        creditCardRepository.addCreditCard(newCard)
        println("Added Credit Card: $newCard")

        // Read all credit cards
        val allCards = creditCardRepository.getAllCreditCards()
        println("All Credit Cards: $allCards")

        // Read a specific credit card by ID (assuming the new ID is 1)
        val cardIdToRead = 1
        val cardRead = creditCardRepository.getCreditCardById(cardIdToRead)
        println("Read Credit Card with ID $cardIdToRead: $cardRead")

        // Update the credit card
        val updatedCard = newCard.copy(limiteDisponivel = 1500.0)
        creditCardRepository.updateCreditCard(cardIdToRead, updatedCard)
        println("Updated Credit Card: $updatedCard")

        // Delete the credit card
        creditCardRepository.deleteCreditCard(cardIdToRead)
        println("Deleted Credit Card with ID $cardIdToRead")
    }
}
