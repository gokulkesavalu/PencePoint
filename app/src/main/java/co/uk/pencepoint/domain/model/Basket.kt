package co.uk.pencepoint.domain.model

/**
 * Domain model representing a shopping basket.
 *
 * @property items The list of [BasketItem] currently in the basket.
 */
data class Basket(
    val items: List<BasketItem> = emptyList()
) {
    /**
     * The sum of all item subtotals (price * quantity) before tax.
     */
    val subtotal: Money
        get() = items.fold(Money.ZERO) { total, item -> total + item.subtotal }

    /**
     * The sum of all tax amounts for each item in the basket.
     */
    val totalTax: Money
        get() = items.fold(Money.ZERO) { total, item -> total + item.taxAmount }

    /**
     * The final total price (Subtotal + Tax).
     */
    val grandTotal: Money
        get() = subtotal + totalTax

    /**
     * Total number of individual items in the basket.
     */
    val itemCount: Int
        get() = items.sumOf { it.quantity }
}
