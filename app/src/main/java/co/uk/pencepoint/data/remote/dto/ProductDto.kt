package co.uk.pencepoint.data.remote.dto

/**
 * Data Transfer Object for Product from FakeStore API.
 * This class matches the external JSON structure.
 */
data class ProductDto(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String,
    val rating: RatingDto? = null
)

data class RatingDto(
    val rate: Double,
    val count: Int
)
