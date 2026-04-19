package co.uk.pencepoint.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import co.uk.pencepoint.data.local.entities.BasketItemEntity
import co.uk.pencepoint.data.local.entities.ProductEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for the basket_items table.
 */
@Dao
interface BasketDao {

    /**
     * Observes all items in the basket.
     *
     * @return A [Flow] emitting a list of [BasketItemEntity] whenever the basket table changes.
     */
    @Query("SELECT * FROM basket_items")
    fun observeBasketItems(): Flow<List<BasketItemEntity>>

    /**
     * Adds a new item to the basket.
     *
     * @param product The product information.
     * @param quantity The number of units to add.
     */
    @Query("INSERT INTO basket_items (product, quantity) VALUES (:product, :quantity)")
    suspend fun addToBasket(product: ProductEntity, quantity: Int)

    /**
     * Updates the quantity of an existing basket item.
     *
     * @param id The unique identifier of the basket item.
     * @param quantity The new quantity for the item.
     */
    @Query("UPDATE basket_items SET quantity = :quantity WHERE id = :id")
    suspend fun updateQuantity(id: Long, quantity: Int)

    /**
     * Removes an item from the basket.
     *
     * @param id The unique identifier of the basket item to remove.
     */
    @Query("DELETE FROM basket_items WHERE id = :id")
    suspend fun removeFromBasket(id: Long)

    /**
     * Clears all items from the basket.
     */
    @Query("DELETE FROM basket_items")
    suspend fun clearBasket()
}
