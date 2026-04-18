package co.uk.pencepoint.domain.repository

import co.uk.pencepoint.domain.model.BasketItem
import co.uk.pencepoint.domain.model.Product
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing the shopping basket.
 * Provides methods to observe and modify the basket contents.
 */
interface BasketRepository {

    /**
     * Returns a [Flow] that emits the current list of [BasketItem]s whenever the basket changes.
     */
    fun getBasketItems(): Flow<List<BasketItem>>

    /**
     * Adds a [product] to the basket with the specified [quantity].
     *
     * @param product The product to add.
     * @param quantity The number of units to add.
     * @return A [Result] indicating success or failure.
     */
    suspend fun addToBasket(product: Product, quantity: Int): Result<Unit>

    /**
     * Updates the quantity of an item in the basket.
     *
     * @param id The unique identifier of the basket item record.
     * @param quantity The new quantity.
     * @return A [Result] indicating success or failure.
     */
    suspend fun updateQuantity(id: Long, quantity: Int): Result<Unit>

    /**
     * Removes an item from the basket.
     *
     * @param id The unique identifier of the basket item record to remove.
     * @return A [Result] indicating success or failure.
     */
    suspend fun removeFromBasket(id: Long): Result<Unit>
}
