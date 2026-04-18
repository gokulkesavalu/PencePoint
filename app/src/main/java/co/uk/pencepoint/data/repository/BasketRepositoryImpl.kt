package co.uk.pencepoint.data.repository

import co.uk.pencepoint.data.local.dao.BasketDao
import co.uk.pencepoint.data.mapper.toDomainModel
import co.uk.pencepoint.data.mapper.toEntity
import co.uk.pencepoint.domain.model.BasketItem
import co.uk.pencepoint.domain.model.Product
import co.uk.pencepoint.domain.repository.BasketRepository
import co.uk.pencepoint.domain.repository.TaxProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of [BasketRepository] using a Room database.
 *
 * @property basketDao The DAO for basket-related operations.
 * @property taxProvider The provider for tax-related calculations and data.
 */
class BasketRepositoryImpl @Inject constructor(
    private val basketDao: BasketDao,
    private val taxProvider: TaxProvider
) : BasketRepository {

    /**
     * Observes the basket items from the local database and maps them to domain models.
     *
     * @return A [Flow] emitting the current list of [BasketItem]s.
     */
    override fun getBasketItems(): Flow<List<BasketItem>> {
        return basketDao.observeBasketItems().map { entities ->
            entities.map { entity ->
                BasketItem(
                    id = entity.id,
                    product = entity.product.toDomainModel(taxProvider),
                    quantity = entity.quantity
                )
            }
        }
    }

    /**
     * Adds a product to the basket.
     *
     * @param product The product to add.
     * @param quantity The initial quantity.
     * @return A [Result] of [Unit] indicating success or failure.
     */
    override suspend fun addToBasket(
        product: Product,
        quantity: Int
    ): Result<Unit> {
        return try {
            basketDao.addToBasket(
                product.toEntity(),
                quantity
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Updates the quantity of an item in the basket.
     *
     * @param id The ID of the basket item record.
     * @param quantity The new quantity.
     * @return A [Result] of [Unit] indicating success or failure.
     */
    override suspend fun updateQuantity(
        id: Long,
        quantity: Int
    ): Result<Unit> {
        return try {
            basketDao.updateQuantity(
                id,
                quantity
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Removes an item from the basket.
     *
     * @param id The ID of the basket item record.
     * @return A [Result] of [Unit] indicating success or failure.
     */
    override suspend fun removeFromBasket(id: Long): Result<Unit> {
        return try {
            basketDao.removeFromBasket(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}