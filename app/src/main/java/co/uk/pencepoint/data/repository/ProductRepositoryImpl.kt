package co.uk.pencepoint.data.repository

import co.uk.pencepoint.data.mapper.toDomainModel
import co.uk.pencepoint.data.remote.FakeStoreApi
import co.uk.pencepoint.domain.model.Product
import co.uk.pencepoint.domain.repository.ProductRepository
import co.uk.pencepoint.domain.repository.TaxProvider
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val api: FakeStoreApi,
    private val taxProvider: TaxProvider
) : ProductRepository {
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