package co.uk.pencepoint.domain.model

import kotlin.math.roundToLong

data class BasketItem(
    val product: Product,
    val quantity: Int
) {
    val subtotal: Money
        get() = product.price * quantity
    
    val taxAmount: Money
        get() {
            val taxPence = (subtotal.amountInPence * (product.taxRate / 100.0)).roundToLong()
            return Money(taxPence)
        }
}
