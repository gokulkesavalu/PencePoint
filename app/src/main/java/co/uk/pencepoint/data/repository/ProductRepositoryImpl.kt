package co.uk.pencepoint.data.repository

import co.uk.pencepoint.data.mapper.toDomainModel
import co.uk.pencepoint.data.remote.FakeStoreApi
import co.uk.pencepoint.domain.model.Product
import co.uk.pencepoint.domain.repository.ProductRepository
import co.uk.pencepoint.domain.repository.TaxProvider
import javax.inject.Inject

/**
 * Implementation of [ProductRepository] that fetches data from the [FakeStoreApi].
 *
 * @property api The remote API for products.
 * @property taxProvider Provider for calculating tax rates based on categories.
 */
class ProductRepositoryImpl @Inject constructor(
    private val api: FakeStoreApi,
    private val taxProvider: TaxProvider
) : ProductRepository {
    /**
     * Retrieves products from the remote API and maps them to domain models.
     *
     * @param category Optional category string to filter products.
     * @return A list of domain [Product] objects.
     */
    override suspend fun getProducts(category: String?): List<Product> {
        return try {
            val products = if (category.isNullOrBlank()) {
                api.getProducts()
            } else {
                api.getProducts(category)
            }
            products.map { it.toDomainModel(taxProvider) }
        } catch (e: Exception) {
            emptyList()
        }
    }
}