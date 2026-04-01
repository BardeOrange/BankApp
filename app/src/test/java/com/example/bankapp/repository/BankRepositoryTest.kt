package com.example.bankapp.repository

import com.example.bankapp.data.repository.BankRepository
import kotlinx.coroutines.test.*
import org.junit.Test
import org.junit.Assert.*

class BankRepositoryTest {

    private val repository = BankRepository()

    @Test
    fun `authenticate with valid login must return true`() = runTest {
        // WHEN
        val result = repository.authenticate("test", "1234")

        // THEN
        assertTrue(result.isSuccess)
        assertTrue(result.getOrNull() == true)
    }

    @Test
    fun `authenticate with empty login must return an error`() = runTest {
        // WHEN
        val result = repository.authenticate("", "1234")

        // THEN
        assertTrue(result.isFailure)
    }

    @Test
    fun `authenticate with to short password must return an error`() = runTest {
        // WHEN
        val result = repository.authenticate("test", "123") // < 4 caractères

        // THEN
        assertTrue(result.isFailure)
    }

    @Test
    fun `getAccounts should return 3 counts`() = runTest {
        // WHEN
        val result = repository.getAccounts()

        // THEN
        assertTrue(result.isSuccess)
        assertEquals(3, result.getOrNull()?.size)
    }

    @Test
    fun `getTransactions must return 10 transactions`() = runTest {
        // WHEN
        val result = repository.getTransactions("ACC001")

        // THEN
        assertTrue(result.isSuccess)
        assertEquals(10, result.getOrNull()?.size)
    }
}