package com.example.bankapp.viewmodel

import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import com.example.bankapp.data.repository.BankRepository
import com.example.bankapp.data.model.*
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class BankViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: BankRepository
    private lateinit var viewModel: BankViewModel

    private val fakeAccounts = listOf(
        Account("ACC001", "Jean Dupont", "FR76...", 3240.50, type = AccountType.CHECKING),
        Account("ACC002", "Jean Dupont", "FR76...", 12800.0, type = AccountType.SAVINGS)
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        viewModel = BankViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // Login tests

    @Test
    fun `initial state must be Idle`() {
        assertTrue(viewModel.authState.value is AuthState.Idle)
    }

    @Test
    fun `login with valid credentials must return Success`() = runTest {
        coEvery { repository.authenticate("test", "1234") } returns Result.success(true)
        coEvery { repository.getAccounts() } returns Result.success(fakeAccounts)

        viewModel.login("test", "1234")
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.authState.value is AuthState.Success)
    }

    @Test
    fun `login with invalid credentials must return Error`() = runTest {
        coEvery {
            repository.authenticate("", "123")
        } returns Result.failure(Exception("Identifiants incorrects"))

        viewModel.login("", "123")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.authState.value
        assertTrue(state is AuthState.Error)
        assertEquals("Identifiants incorrects", (state as AuthState.Error).message)
    }

    @Test
    fun `logout must bring back the Idle state`() = runTest {
        coEvery { repository.authenticate(any(), any()) } returns Result.success(true)
        coEvery { repository.getAccounts() } returns Result.success(fakeAccounts)

        viewModel.login("test", "1234")
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.logout()

        assertTrue(viewModel.authState.value is AuthState.Idle)
    }

    // ---- Tests des comptes ----

    @Test
    fun `getAccounts must return the account list`() = runTest {
        coEvery { repository.authenticate(any(), any()) } returns Result.success(true)
        coEvery { repository.getAccounts() } returns Result.success(fakeAccounts)

        viewModel.login("test", "1234")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.accountsState.value
        assertTrue(state is AccountsState.Success)
        assertEquals(2, (state as AccountsState.Success).accounts.size)
    }

    @Test
    fun `getAccounts error should return AccountsState Error`() = runTest {
        coEvery { repository.authenticate(any(), any()) } returns Result.success(true)
        coEvery { repository.getAccounts() } returns Result.failure(Exception("Erreur réseau"))

        viewModel.login("test", "1234")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.accountsState.value
        assertTrue(state is AccountsState.Error)
        assertEquals("Erreur réseau", (state as AccountsState.Error).message)
    }

    // ---- Tests des transactions ----

    @Test
    fun `loadTransactions should update selectedAccount`() = runTest {
        val account = fakeAccounts.first()
        coEvery { repository.getTransactions(any()) } returns Result.success(emptyList())

        viewModel.loadTransactions(account)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(account, viewModel.selectedAccount.value)
    }
}