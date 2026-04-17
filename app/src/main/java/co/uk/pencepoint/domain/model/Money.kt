package co.uk.pencepoint.domain.model

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

/**
 * A domain model representing money in a POS system.
 * We store the value as a Long in the smallest unit (e.g., pence/cents)
 * to avoid floating-point errors associated with Double.
 *
 * @property amountInPence The monetary value in the smallest unit (e.g., pence).
 * @property currencyCode The ISO 4217 currency code (default is "GBP").
 */
data class Money(
    val amountInPence: Long,
    val currencyCode: String = "GBP"
) {
    /**
     * Returns a localized string representation of the money value.
     */
    val displayValue: String
        get() {
            val formatter = NumberFormat.getCurrencyInstance(Locale.getDefault())
            formatter.currency = Currency.getInstance(currencyCode)
            return formatter.format(amountInPence / 100.0)
        }

    /**
     * Adds another [Money] instance to this one.
     * @throws IllegalArgumentException if currency codes do not match.
     */
    operator fun plus(other: Money): Money {
        require(currencyCode == other.currencyCode) { "Cannot add different currencies" }
        return Money(amountInPence + other.amountInPence, currencyCode)
    }

    /**
     * Subtracts another [Money] instance from this one.
     * @throws IllegalArgumentException if currency codes do not match.
     */
    operator fun minus(other: Money): Money {
        require(currencyCode == other.currencyCode) { "Cannot subtract different currencies" }
        return Money(amountInPence - other.amountInPence, currencyCode)
    }

    /**
     * Multiplies the money value by a given quantity.
     */
    operator fun times(quantity: Int): Money {
        return Money(amountInPence * quantity, currencyCode)
    }

    companion object {
        /**
         * Represents a zero money value in GBP.
         */
        val ZERO = Money(0)
    }
}
