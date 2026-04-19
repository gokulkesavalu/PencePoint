package co.uk.pencepoint.data.repository

import co.uk.pencepoint.data.local.dao.TransactionDao
import co.uk.pencepoint.data.mapper.toDomain
import co.uk.pencepoint.data.mapper.toEntity
import co.uk.pencepoint.domain.model.Transaction
import co.uk.pencepoint.domain.model.TransactionStatus
import co.uk.pencepoint.domain.repository.TaxProvider
import co.uk.pencepoint.domain.repository.TransactionRepository
import javax.inject.Inject

/**
 * Implementation of [TransactionRepository] using Room database for local persistence.
 *
 * @property transactionDao The Data Access Object for transaction operations.
 * @property taxProvider The provider used to calculate tax during domain mapping.
 */
class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao,
    private val taxProvider: TaxProvider
) : TransactionRepository {

    /**
     * Persists a transaction to the local database.
     *
     * @param transaction The [Transaction] domain model to save.
     * @return A [Result] indicating success or containing an error.
     */
    override suspend fun saveTransaction(transaction: Transaction): Result<Unit> {
        return try {
            transactionDao.insertTransaction(transaction.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Retrieves transactions from the local database, optionally filtered by status.
     *
     * @param status The optional [TransactionStatus] to filter by.
     * @return A [Result] containing the list of mapped [Transaction] models.
     */
    override suspend fun getTransactions(status: TransactionStatus?): Result<List<Transaction>> {
        return try {
            val statusString = status?.name
            val entities = transactionDao.getTransactions(statusString)
            val transactions = entities.map { it.toDomain(taxProvider) }
            Result.success(transactions)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Retrieves a single transaction by its unique order number.
     *
     * @param orderNumber The order number to search for.
     * @return A [Result] containing the transaction if found, or an error otherwise.
     */
    override suspend fun getTransactionByOrderNumber(orderNumber: Long): Result<Transaction> {
        return try {
            val entity = transactionDao.getTransactionByOrderNumber(orderNumber)
            if (entity != null) {
                Result.success(entity.toDomain(taxProvider))
            } else {
                Result.failure(Exception("Transaction not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Retrieves transactions that occurred within a specific date range.
     *
     * @param startDate The start timestamp in milliseconds.
     * @param endDate The end timestamp in milliseconds.
     * @return A [Result] containing the list of transactions found in the range.
     */
    override suspend fun getTransactionsByDateRange(
        startDate: Long,
        endDate: Long
    ): Result<List<Transaction>> {
        return try {
            val entities = transactionDao.getTransactionsByDateRange(startDate, endDate)
            val transactions = entities.map { it.toDomain(taxProvider) }
            Result.success(transactions)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
