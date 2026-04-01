package com.example.bankapp.data.model

import java.time.LocalDate

data class Transaction(
    val id: String,
    val label: String,
    val amount: Double,
    val date: LocalDate,
    val category: TransactionCategory,
    val isCredit: Boolean
)

enum class TransactionCategory(val label: String, val icon: String) {
    SHOPPING("Shopping", "🛍️"),
    FOOD("Alimentation", "🍔"),
    TRANSPORT("Transport", "🚗"),
    HEALTH("Santé", "💊"),
    SALARY("Salaire", "💼"),
    TRANSFER("Virement", "↔️"),
    OTHER("Autre", "📦")
}