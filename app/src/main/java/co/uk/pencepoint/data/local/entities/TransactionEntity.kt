package co.uk.pencepoint.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room Entity representing a completed or pending transaction record.
 *
 * @property id Unique identifier for the transaction record in the database.
 * @property orderNumber A business-facing unique order number.
 * @property items The list of items included in this transaction.
 * @property subTotal The total amount before tax, stored in pence.
 * @property tax The total tax amount, stored in pence.
 * @property total The final transaction amount including tax, stored in pence.
 * @property status The current status of the transaction (e.g., PENDING, COMPLETED).
 * @property transactionReference A unique reference string for external tracking (e.g., payment gateway ID).
 * @property timestamp The time at which the transaction record was created.
 */
@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val orderNumber: Long,
    val items: List<BasketItemEntity>,
    val subTotal: Long,
    val tax: Long,
    val total: Long,
    val status: String,
    val transactionReference: String,
    val timestamp: Long = System.currentTimeMillis(),
)
