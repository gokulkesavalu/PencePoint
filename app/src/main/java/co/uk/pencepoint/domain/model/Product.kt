package co.uk.pencepoint.domain.model

/**
 * A domain model representing a Product in our POS system.
 * This is decoupled from the data layer (DTOs).
 *
 * It is a "pure" data model, meaning it doesn't contain business logic
 * like tax calculation. That logic is handled by a TaxProvider.
 */
data class Product(
    val id: Long,
    val title: String,
    val price: Money,
    val description: String,
    val category: Category,
    val imageUrl: String,
    val taxRate: Double
)

enum class Category {
    ELECTRONICS,
    CLOTHING,
    FOOD,
    BOOKS,
    JEWELLERY,
    OTHER;
}
