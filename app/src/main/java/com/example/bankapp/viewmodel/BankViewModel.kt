package com.example.bankapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bankapp.data.model.Account
import com.example.bankapp.data.model.Transaction
import com.example.bankapp.data.repository.BankRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// UI States

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}

sealed class AccountsState {
    object Loading : AccountsState()
    data class Success(val accounts: List<Account>) : AccountsState()
    data class Error(val message: String) : AccountsState()
}

sealed class TransactionsState {
    object Idle : TransactionsState()
    object Loading : TransactionsState()
    data class Success(val transactions: List<Transaction>) : TransactionsState()
    data class Error(val message: String) : TransactionsState()
}

// ViewModel

class BankViewModel(
    private val repository: BankRepository = BankRepository()
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _accountsState = MutableStateFlow<AccountsState>(AccountsState.Loading)
    val accountsState: StateFlow<AccountsState> = _accountsState.asStateFlow()

    private val _transactionsState = MutableStateFlow<TransactionsState>(TransactionsState.Idle)
    val transactionsState: StateFlow<TransactionsState> = _transactionsState.asStateFlow()

    private val _selectedAccount = MutableStateFlow<Account?>(null)
    val selectedAccount: StateFlow<Account?> = _selectedAccount.asStateFlow()

    // Authentification
    fun login(login: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            repository.authenticate(login, password)
                .onSuccess {
                    _authState.value = AuthState.Success
                    loadAccounts()
                }
                .onFailure { e ->
                    _authState.value = AuthState.Error(e.message ?: "Erreur inconnue")
                }
        }
    }

    fun logout() {
        _authState.value = AuthState.Idle
        _accountsState.value = AccountsState.Loading
        _transactionsState.value = TransactionsState.Idle
    }

    // Accounts load
    private fun loadAccounts() {
        viewModelScope.launch {
            _accountsState.value = AccountsState.Loading
            repository.getAccounts()
                .onSuccess { accounts ->
                    _accountsState.value = AccountsState.Success(accounts)
                }
                .onFailure { e ->
                    _accountsState.value = AccountsState.Error(e.message ?: "Erreur chargement")
                }
        }
    }

    // Transactions load
    fun loadTransactions(account: Account) {
        _selectedAccount.value = account
        viewModelScope.launch {
            _transactionsState.value = TransactionsState.Loading
            repository.getTransactions(account.id)
                .onSuccess { transactions ->
                    _transactionsState.value = TransactionsState.Success(transactions)
                }
                .onFailure { e ->
                    _transactionsState.value = TransactionsState.Error(e.message ?: "Erreur")
                }
        }
    }
}