package com.example.bankapp.data.model

data class Account(
    val id: String,
    val ownerName: String,
    val iban: String,
    val balance: Double,
    val currency: String = "EUR",
    val type: AccountType
)

enum class AccountType(val label: String) {
    CHECKING("Compte courant"),
    SAVINGS("Livret A"),
    BUSINESS("Compte professionnel")
}