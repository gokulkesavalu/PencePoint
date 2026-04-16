package co.uk.pencepoint.domain.repository

import co.uk.pencepoint.domain.model.Category
import co.uk.pencepoint.domain.model.Money

/**
 * Interface for providing tax rates based on product categories and potentially other factors
 * like price or region.
 */
interface TaxProvider {
    /**
     * Returns the tax rate percentage (e.g., 20.0 for 20%) for a given category and price.
     */
    fun getTaxRate(category: Category, price: Money): Double
}
