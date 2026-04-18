package co.uk.pencepoint.data.remote.dto

/**
 * Data Transfer Object for Product from FakeStore API.
 * This class matches the external JSON structure.
 *
 * @property id The product ID.
 * @property title The product title.
 * @property price The product price as a Double.
 * @property description A brief description of the product.
 * @property category The category name from the API.
 * @property image The URL of the product image.
 * @property rating Rating information if available.
 */
data class ProductDto(
    val id: Long,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String,
    val rating: RatingDto? = null
)

/**
 * Data Transfer Object for product ratings.
 *
 * @property rate The average rating score.
 * @property count The number of reviews.
 */
data class RatingDto(
    val rate: Double,
    val count: Int
)
