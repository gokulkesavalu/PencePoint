package co.uk.pencepoint.data.repository

import co.uk.pencepoint.domain.model.Category
import co.uk.pencepoint.domain.model.Money
import co.uk.pencepoint.domain.repository.TaxProvider
import javax.inject.Inject

/**
 * A local implementation of [TaxProvider] that provides static tax rates based on product categories.
 */
class LocalTaxProvider @Inject constructor() : TaxProvider {
    /**
     * Calculates the tax rate for a given category and price.
     *
     * @param category The product category.
     * @param price The price of the product.
     * @return The tax rate as a percentage (e.g., 20.0 for 20%).
     */
    override fun getTaxRate(
        category: Category,
        price: Money
    ): Double {
        return when (category) {
            Category.ELECTRONICS -> 15.0
            Category.CLOTHING -> 10.0
            Category.FOOD -> 5.0
            Category.BOOKS -> 3.0
            Category.JEWELLERY -> 20.0
            else -> 0.0
        }
    }
}