package co.uk.pencepoint.data.repository

import co.uk.pencepoint.domain.model.Category
import co.uk.pencepoint.domain.model.Money
import co.uk.pencepoint.domain.repository.TaxProvider
import javax.inject.Inject

class LocalTaxProvider @Inject constructor() : TaxProvider {
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