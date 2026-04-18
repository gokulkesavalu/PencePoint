package co.uk.pencepoint.data.mapper

import co.uk.pencepoint.data.local.entities.BasketItemEntity
import co.uk.pencepoint.domain.model.BasketItem
import co.uk.pencepoint.domain.repository.TaxProvider

/**
 * Mappers for basket-related data between data and domain layers.
 */

/**
 * Converts a [BasketItemEntity] to its domain model [BasketItem].
 *
 * @param taxProvider The [TaxProvider] used to determine the tax rate for the product.
 * @return A [BasketItem] containing domain model product details and quantity.
 */
fun BasketItemEntity.toDomainModel(taxProvider: TaxProvider) = BasketItem(
    id = id,
    product = product.toDomainModel(taxProvider),
    quantity = quantity
)

/**
 * Converts a domain model [BasketItem] to its Room entity [BasketItemEntity].
 *
 * @return A [BasketItemEntity] suitable for local storage.
 */
fun BasketItem.toEntity() = BasketItemEntity(
    id = id,
    product = product.toEntity(),
    quantity = quantity
)