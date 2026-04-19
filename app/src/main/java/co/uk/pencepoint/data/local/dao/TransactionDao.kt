package co.uk.pencepoint.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import co.uk.pencepoint.data.local.entities.TransactionEntity

/**
 * Data Access Object for managing transaction history in the local database.
 */
@Dao
interface TransactionDao {
    /**
     * Inserts a new transaction record.
     *
     * @param transaction The [TransactionEntity] to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity)

    /**
     * Retrieves transactions from the database, optionally filtered by status.
     *
     * @param status The status string to filter by. If null, all transactions are returned.
     * @return A list of [TransactionEntity] records, ordered by most recent first.
     */
    @Query("SELECT * FROM transactions WHERE (:status IS NULL OR status = :status) ORDER BY timestamp DESC")
    suspend fun getTransactions(status: String?): List<TransactionEntity>

    /**
     * Finds a specific transaction by its unique order number.
     *
     * @param orderNumber The order number to search for.
     * @return The matching [TransactionEntity], or null if not found.
     */
    @Query("SELECT * FROM transactions WHERE orderNumber = :orderNumber")
    suspend fun getTransactionByOrderNumber(orderNumber: Long): TransactionEntity?

    /**
     * Retrieves transactions that occurred within a specific time period.
     *
     * @param startDate The start of the time range (inclusive) in milliseconds.
     * @param endDate The end of the time range (inclusive) in milliseconds.
     * @return A list of [TransactionEntity] records within the specified date range.
     */
    @Query("SELECT * FROM transactions WHERE timestamp BETWEEN :startDate AND :endDate")
    suspend fun getTransactionsByDateRange(startDate: Long, endDate: Long): List<TransactionEntity>
}
