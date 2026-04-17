package co.uk.pencepoint.domain.repository

import co.uk.pencepoint.domain.model.Product

/**
 * Repository interface for managing [Product] data.
 * This acts as the single source of truth for product-related information
 * in the domain layer, decoupling it from data sources like remote APIs or local databases.
 */
interface ProductRepository {
    /**
     * Fetches products from the data source.
     *
     * @param category If provided, filters products by this category string (e.g., "electronics", "grocery").
     *                 The implementation handles mapping or normalizing these strings to the internal
     *                 domain categories. If null, all available products are returned.
     * @return A [Result] containing a list of [Product] objects.
     */
    suspend fun getProducts(category: String? = null): Result<List<Product>>
}
