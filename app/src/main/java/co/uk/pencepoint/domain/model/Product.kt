package co.uk.pencepoint.domain.model

/**
 * A domain model representing a Product in our POS system.
 * This is decoupled from the data layer (DTOs).
 *
 * It is a "pure" data model, meaning it doesn't contain business logic
 * like tax calculation. That logic is handled by a TaxProvider.
 *
 * @property id Unique identifier for the product.
 * @property title The name of the product.
 * @property price The base price of the product using the [Money] value object.
 * @property description A detailed description of the product.
 * @property category The category the product belongs to, used for tax calculation.
 * @property imageUrl URL to the product image.
 * @property taxRate The current tax rate applied to this product instance.
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

/**
 * Categories for products in the POS system.
 */
enum class Category {
    ELECTRONICS,
    CLOTHING,
    FOOD,
    BOOKS,
    JEWELLERY,
    OTHER;
}
