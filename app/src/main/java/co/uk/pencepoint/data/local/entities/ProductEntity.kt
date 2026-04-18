package co.uk.pencepoint.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room Entity representing a product in the local database.
 *
 * @property id Unique identifier for the product.
 * @property title The name of the product.
 * @property price The price of the product (from the remote API, typically as a Double).
 * @property description A detailed description of the product.
 * @property category The category the product belongs to.
 * @property image URL to the product image.
 * @property rating Rating information for the product.
 * @property cachedAt Timestamp when the product was saved to the local database.
 */
@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = false) val id: Long,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String,
    val rating: RatingEntity,
    val cachedAt: Long = System.currentTimeMillis()
)

/**
 * Embedded entity representing the rating of a product.
 *
 * @property rate The average rating score.
 * @property count The number of reviews.
 */
data class RatingEntity(
    val rate: Double,
    val count: Int
)