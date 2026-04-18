package co.uk.pencepoint.domain.model

import kotlin.math.roundToLong

/**
 * Domain model representing an item in the shopping basket.
 *
 * @property product The [Product] associated with this basket item.
 * @property quantity The number of units of the product in the basket.
 */
data class BasketItem(
    val id: Long,
    val product: Product,
    val quantity: Int
) {
    /**
     * The subtotal for this item (price * quantity) before tax.
     */
    val subtotal: Money
        get() = product.price * quantity

    /**
     * The total tax amount for this item based on the product's tax rate.
     */
    val taxAmount: Money
        get() {
            val taxPence = (subtotal.amountInPence * (product.taxRate / 100.0)).roundToLong()
            return Money(taxPence)
        }
}
