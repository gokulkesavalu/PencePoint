package co.uk.pencepoint.data.repository

import co.uk.pencepoint.data.local.dao.ProductDao
import co.uk.pencepoint.data.mapper.toDomainModel
import co.uk.pencepoint.data.mapper.toEntity
import co.uk.pencepoint.data.remote.FakeStoreApi
import co.uk.pencepoint.domain.model.Product
import co.uk.pencepoint.domain.repository.ProductRepository
import co.uk.pencepoint.domain.repository.TaxProvider
import java.util.concurrent.CancellationException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Implementation of [ProductRepository] that handles product data operations.
 * It uses [FakeStoreApi] as the remote data source and [ProductDao] for local caching,
 * following an offline-first approach with a cache timeout.
 *
 * @property api The remote API for products.
 * @property productDao The local DAO for product caching.
 * @property taxProvider Provider for calculating tax rates based on categories.
 */
class ProductRepositoryImpl @Inject constructor(
    private val api: FakeStoreApi,
    private val productDao: ProductDao,
    private val taxProvider: TaxProvider
) : ProductRepository {

    companion object {
        private val CACHE_TIMEOUT = TimeUnit.MINUTES.toMillis(10)
    }

    /**
     * Retrieves products from the data sources (local cache or remote API).
     *
     * @param category Optional category string to filter products.
     * @return A [Result] containing a list of domain [Product] objects.
     */
    override suspend fun getProducts(category: String?): Result<List<Product>> {
        val cachedProducts = if (category.isNullOrEmpty()) {
            productDao.getProducts()
        } else {
            productDao.getProductsByCategory(category)
        }
        val isCacheValid =
            cachedProducts.isNotEmpty() && System.currentTimeMillis() - cachedProducts.first().cachedAt < CACHE_TIMEOUT
        return try {
            if (isCacheValid) {
                Result.success(cachedProducts.map { it.toDomainModel(taxProvider) })
            } else {
                val updatedProducts = if (category.isNullOrEmpty()) {
                    api.getProducts()
                } else {
                    api.getProducts(category)
                }
                productDao.insertProducts(updatedProducts.map { it.toEntity() })
                Result.success(updatedProducts.map { it.toDomainModel(taxProvider) })
            }
        } catch (e: Exception) {
            if (e is CancellationException) throw e // Propagate cancellation
            Result.success(cachedProducts.map { it.toDomainModel(taxProvider) })
        }
    }

    override suspend fun getProductDetails(id: Long): Result<Product> {
        val cachedProduct = productDao.getProducts().firstOrNull { it.id == id }
        val isCacheValid =
            cachedProduct != null && System.currentTimeMillis() - cachedProduct.cachedAt < CACHE_TIMEOUT
        return try {
            if (isCacheValid) {
                Result.success(cachedProduct.toDomainModel(taxProvider))
            } else {
                val updatedProduct = api.getProductDetails(id)
                Result.success(updatedProduct.toDomainModel(taxProvider))
            }

        } catch (e: Exception) {
            return if (isCacheValid) {
                Result.success(cachedProduct.toDomainModel(taxProvider))
            } else {
                Result.failure(e)
            }
        }
    }
}