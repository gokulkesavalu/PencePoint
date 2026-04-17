package co.uk.pencepoint.data.mapper

import co.uk.pencepoint.data.local.entities.ProductEntity
import co.uk.pencepoint.data.local.entities.RatingEntity
import co.uk.pencepoint.data.remote.dto.ProductDto
import co.uk.pencepoint.domain.model.Category
import co.uk.pencepoint.domain.model.Money
import co.uk.pencepoint.domain.model.Product
import co.uk.pencepoint.domain.repository.TaxProvider
import kotlin.math.roundToLong

/**
 * Mapper functions to convert Data Transfer Objects (DTOs) to Domain models.
 */

/**
 * Converts a [ProductDto] to a domain [Product].
 *
 * @param taxProvider The [TaxProvider] used to determine the tax rate for the product.
 * @return A [Product] domain model.
 */
fun ProductDto.toDomainModel(taxProvider: TaxProvider) = Product(
    id = id.toLong(),
    title = title,
    description = description,
    price = calculatePrice(price),
    category = mapToCategory(category),
    imageUrl = image,
    taxRate = taxProvider.getTaxRate(mapToCategory(category), calculatePrice(price))
)


/**
 * Converts a [ProductEntity] to a domain [Product].
 *
 * @param taxProvider The [TaxProvider] used to determine the tax rate for the product.
 * @return A [Product] domain model.
 */
fun ProductEntity.toDomainModel(taxProvider: TaxProvider) = Product(
    id = id.toLong(),
    title = title,
    description = description,
    price = calculatePrice(price),
    category = mapToCategory(category),
    imageUrl = image,
    taxRate = taxProvider.getTaxRate(mapToCategory(category), calculatePrice(price))
)


/**
 * Converts a [ProductDto] to a [ProductEntity] for local storage.
 *
 * @return A [ProductEntity] suitable for Room database persistence.
 */
fun ProductDto.toEntity() = ProductEntity(
    id = id,
    title = title,
    description = description,
    price = price,
    category = category,
    image = image,
    rating = RatingEntity(rate = rating?.rate ?: 0.0, count = rating?.count ?: 0),
    cachedAt = System.currentTimeMillis()
)

/**
 * Maps a category string from the API to the internal [Category] enum.
 *
 * @param category The category string to map.
 * @return The corresponding [Category] enum.
 */
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

/**
 * Converts a price in [Double] (as received from the API) to a [Money] object in pence.
 *
 * @param price The price as a [Double].
 * @return A [Money] object representing the price in pence.
 */
private fun calculatePrice(price: Double) = Money((price * 100.0).roundToLong())

