package com.example.bankapp.data.repository

import com.example.bankapp.data.model.*
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.util.UUID

/**
 * Repository simulating banking datas
 */
class BankRepository {

    // Simulation of a network call with Coroutines
    suspend fun getAccounts(): Result<List<Account>> {
        return try {
            delay(800) // Simulating lag
            Result.success(fakeAccounts())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTransactions(accountId: String): Result<List<Transaction>> {
        return try {
            delay(600)
            Result.success(fakeTransactions())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun authenticate(login: String, password: String): Result<Boolean> {
        return try {
            delay(1000)
            // Simulation : accepte n'importe quel login non vide
            if (login.isNotBlank() && password.length >= 4) {
                Result.success(true)
            } else {
                Result.failure(Exception("Identifiants incorrects"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Fake datas

    private fun fakeAccounts() = listOf(
        Account(
            id = "ACC001",
            ownerName = "Jean Dupont",
            iban = "FR76 3000 6000 0112 3456 7890 189",
            balance = 3_240.50,
            type = AccountType.CHECKING
        ),
        Account(
            id = "ACC002",
            ownerName = "Jean Dupont",
            iban = "FR76 3000 6000 0198 7654 3210 189",
            balance = 12_800.00,
            type = AccountType.SAVINGS
        ),
        Account(
            id = "ACC003",
            ownerName = "Jean Dupont",
            iban = "FR76 3000 6000 0155 5555 5555 189",
            balance = 8_450.75,
            type = AccountType.BUSINESS
        )
    )

    private fun fakeTransactions() = listOf(
        Transaction(UUID.randomUUID().toString(), "Virement salaire", 2800.0, LocalDate.now().minusDays(1), TransactionCategory.SALARY, true),
        Transaction(UUID.randomUUID().toString(), "Carrefour Market", -67.40, LocalDate.now().minusDays(2), TransactionCategory.FOOD, false),
        Transaction(UUID.randomUUID().toString(), "Amazon", -129.99, LocalDate.now().minusDays(3), TransactionCategory.SHOPPING, false),
        Transaction(UUID.randomUUID().toString(), "SNCF", -45.00, LocalDate.now().minusDays(4), TransactionCategory.TRANSPORT, false),
        Transaction(UUID.randomUUID().toString(), "Pharmacie Centrale", -18.50, LocalDate.now().minusDays(5), TransactionCategory.HEALTH, false),
        Transaction(UUID.randomUUID().toString(), "Virement reçu", 500.0, LocalDate.now().minusDays(6), TransactionCategory.TRANSFER, true),
        Transaction(UUID.randomUUID().toString(), "Netflix", -15.99, LocalDate.now().minusDays(7), TransactionCategory.OTHER, false),
        Transaction(UUID.randomUUID().toString(), "Uber Eats", -32.10, LocalDate.now().minusDays(8), TransactionCategory.FOOD, false),
        Transaction(UUID.randomUUID().toString(), "Prime mensuelle", -9.99, LocalDate.now().minusDays(9), TransactionCategory.OTHER, false),
        Transaction(UUID.randomUUID().toString(), "Remboursement ami", 50.0, LocalDate.now().minusDays(10), TransactionCategory.TRANSFER, true),
    )
}