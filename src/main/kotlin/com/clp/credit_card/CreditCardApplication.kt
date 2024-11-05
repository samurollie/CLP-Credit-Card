package com.clp.credit_card
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication
@EnableTransactionManagement
class CreditCardApiApplication

fun main(args: Array<String>) {
	runApplication<CreditCardApiApplication>(*args)
}