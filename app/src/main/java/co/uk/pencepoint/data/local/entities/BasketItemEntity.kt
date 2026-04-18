package co.uk.pencepoint.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room Entity representing an item in the user's shopping basket.
 *
 * @property id Unique identifier for the basket item record.
 * @property product The product associated with this basket item.
 * @property quantity The number of units of this product in the basket.
 */
@Entity(tableName = "basket_items")
data class BasketItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val product: ProductEntity,
    val quantity: Int
)
