package co.uk.pencepoint.domain.repository

import co.uk.pencepoint.domain.model.Transaction
import co.uk.pencepoint.domain.model.TransactionStatus

/**
 * Interface defining the contract for managing transactions.
 *
 * This repository handles the persistence and retrieval of transaction history
 * within the domain layer.
 */
interface TransactionRepository {
    /**
     * Persists a new transaction record.
     *
     * @param transaction The [Transaction] to save.
     * @return A [Result] indicating success or failure.
     */
    suspend fun saveTransaction(transaction: Transaction): Result<Unit>

    /**
     * Retrieves all recorded transactions, typically ordered by most recent first.
     *
     * @return A [Result] containing a list of [Transaction]s.
     */
    suspend fun getTransactions(status: TransactionStatus? = null): Result<List<Transaction>>

    /**
     * Finds a specific transaction by its unique order number.
     *
     * @param orderNumber The unique identifier for the order.
     * @return A [Result] containing the matching [Transaction].
     */
    suspend fun getTransactionByOrderNumber(orderNumber: Long): Result<Transaction>

    /**
     * Retrieves transactions that occurred within a specific date range.
     *
     * @param startDate The start of the time range (inclusive) in milliseconds.
     * @param endDate The end of the time range (inclusive) in milliseconds.
     * @return A [Result] containing a list of [Transaction]s within the range.
     */
    suspend fun getTransactionsByDateRange(
        startDate: Long,
        endDate: Long
    ): Result<List<Transaction>>
}
