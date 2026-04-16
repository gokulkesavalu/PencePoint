package co.uk.pencepoint.domain.model

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

/**
 * A domain model representing money in a POS system.
 * We store the value as a Long in the smallest unit (e.g., pence/cents)
 * to avoid floating-point errors associated with Double.
 */
data class Money(
    val amountInPence: Long,
    val currencyCode: String = "GBP"
) {
    val displayValue: String
        get() {
            val formatter = NumberFormat.getCurrencyInstance(Locale.getDefault())
            formatter.currency = Currency.getInstance(currencyCode)
            return formatter.format(amountInPence / 100.0)
        }

    operator fun plus(other: Money): Money {
        require(currencyCode == other.currencyCode) { "Cannot add different currencies" }
        return Money(amountInPence + other.amountInPence, currencyCode)
    }

    operator fun minus(other: Money): Money {
        require(currencyCode == other.currencyCode) { "Cannot subtract different currencies" }
        return Money(amountInPence - other.amountInPence, currencyCode)
    }

    operator fun times(quantity: Int): Money {
        return Money(amountInPence * quantity, currencyCode)
    }

    companion object {
        val ZERO = Money(0)
    }
}
