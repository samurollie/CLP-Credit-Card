package com.clp.models

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException

class DatabaseManager {

    companion object {
        fun connectToDatabase(): Connection? {
            val url = "jdbc:postgresql://localhost:5432/GerenciadorDeCartao"
            val user = "postgres"
            val password = "adminadmin"

            return try {
                DriverManager.getConnection(url, user, password).also {
                    println("Connected to the database!")
                }
            } catch (e: SQLException) {
                println("Connection failed: ${e.message}")
                null
            }
        }

        fun createLine(connection: Connection) {
            val sql = "INSERT INTO usuarios (nome, email) VALUES (?, ?)"

            connection.prepareStatement(sql).use { statement ->
                statement.setString(1, "Marcos")
                statement.setString(2, "marcos@email.com")

                val rowsInserted = statement.executeUpdate()
                println("$rowsInserted row(s) inserted.")
            }
        }

        fun closeConnection(connection: Connection) {
            try {
                connection.close()
                println("Connection closed.")
            } catch (e: SQLException) {
                println("Failed to close connection: ${e.message}")
            }
        }
    }

}