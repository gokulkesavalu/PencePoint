package co.uk.pencepoint.data.mapper

import co.uk.pencepoint.data.remote.dto.ProductDto
import co.uk.pencepoint.domain.model.Category
import co.uk.pencepoint.domain.model.Money
import co.uk.pencepoint.domain.model.Product
import co.uk.pencepoint.domain.repository.TaxProvider
import kotlin.math.roundToLong

fun ProductDto.toDomainModel(taxProvider: TaxProvider) = Product(
    id = id.toLong(),
    title = title,
    description = description,
    price = calculatePrice(price),
    category = mapToCategory(category),
    imageUrl = image,
    taxRate = taxProvider.getTaxRate(mapToCategory(category), calculatePrice(price))
)

private fun mapToCategory(category: String): Category {
    val normalizedCategory = category.lowercase().trim()
    return when {
        normalizedCategory.contains("electronics") -> Category.ELECTRONICS
        normalizedCategory.contains("clothing") -> Category.CLOTHING
        normalizedCategory.contains("food") || normalizedCategory.contains("grocery") || normalizedCategory.contains(
            "drinks"
        ) -> Category.FOOD
        normalizedCategory.contains("books") -> Category.BOOKS
        normalizedCategory.contains("jewel") -> Category.JEWELLERY
        else -> Category.OTHER
    }
}

private fun calculatePrice(price: Double) = Money((price * 100.0).roundToLong())

