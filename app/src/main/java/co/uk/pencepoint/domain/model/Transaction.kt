package co.uk.pencepoint.domain.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Domain model representing a completed or pending transaction.
 *
 * @property id Unique identifier for the transaction record.
 * @property orderNumber A business-facing unique order number.
 * @property items The list of items purchased in this transaction.
 * @property subTotalAmount The total amount before tax.
 * @property totalAmount The final transaction amount including tax.
 * @property taxAmount The total tax calculated for this transaction.
 * @property timestamp The time at which the transaction record was created (in milliseconds).
 * @property status The current lifecycle state of the transaction.
 * @property transactionReference A unique reference string for external tracking (e.g., payment gateway ID).
 */
data class Transaction(
    val id: Long = 0,
    val orderNumber: Long,
    val items: List<BasketItem>,
    val subTotalAmount: Money,
    val totalAmount: Money,
    val taxAmount: Money,
    val timestamp: Long,
    val status: TransactionStatus,
    val transactionReference: String,
) {
    /**
     * Returns a human-readable date and time for the transaction.
     */
    val formattedTimestamp: String
        get() {
            val date = Date(timestamp)
            val format = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
            return format.format(date)
        }

    /**
     * Returns the total number of individual items (sum of quantities).
     */
    val totalItemCount: Int
        get() = items.sumOf { it.quantity }

    val formattedSubTotal: String
        get() = subTotalAmount.displayValue

    val formattedTotal: String
        get() = totalAmount.displayValue

    val formattedTax: String
        get() = taxAmount.displayValue

    /**
     * Returns a user-friendly display string for the transaction status.
     */
    val statusDisplay: String
        get() = status.name.lowercase().replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
        }
}

/**
 * Represents the possible lifecycle states of a transaction in the domain layer.
 */
enum class TransactionStatus {
    PENDING,
    COMPLETED,
    FAILED
}
