# BankApp - Android Banking Application

A Android banking application built to train my skills on Android Developer Analyst position.

---

## Overview

The application simulates a banking app featuring:
- A secure login screen
- A dashboard displaying bank accounts
- A transaction history per account

---

## Tech Stack

| Technology | Usage |
|---|---|
| **Kotlin** | Main language |
| **Jetpack Compose** | Declarative UI framework |
| **MVVM Architecture** | Separation of concerns |
| **Coroutines + StateFlow** | Asynchronous reactive programming |
| **Ktor** | HTTP client |
| **Coil** | Image loading |
| **Navigation Compose** | In-app navigation |
| **Gradle KTS** | Project configuration |
| **JUnit + Mockk** | Unit testing |

---

## Architecture

    app/
    └── src/main/java/com/example/bankapp/
        ├── data/
        │   ├── model/          # Data models (Account, Transaction)
        │   ├── network/        # Ktor HTTP client
        │   └── repository/     # Data access layer
        ├── ui/
        │   ├── screens/        # Compose screens (Login, Dashboard, Transactions)
        │   ├── navigation/     # Navigation system
        │   └── theme/          # Material 3 theme
        └── viewmodel/          # ViewModels (business logic)
            ├── BankViewModel/  # Updates States

---

## Features

### Authentication
- Login form with input validation
- Show/hide password toggle
- State management (Loading, Success, Error)

### Dashboard
- List of bank accounts
- Balance display per account

### Transactions
- Transaction history per account
- Transaction categorization
- Color coding : green (credit) / red (debit)

---

## Tests

```bash
# Run all unit tests
./gradlew test
BankViewModelTest
    - initial state should be Idle
    - login with valid credentials should return Success
    - login with invalid credentials should return Error
    - logout should reset state to Idle
    - getAccounts should return accounts list
    - getAccounts on error should return AccountsState Error
    - loadTransactions should update selectedAccount

BankRepositoryTest
    - authenticate with valid login should return true
    - authenticate with empty login should return an error
    - authenticate with too short password should return an error
    - getAccounts should return 3 accounts
    - getTransactions should return 10 transactions