package co.uk.pencepoint.domain.usecases

import co.uk.pencepoint.domain.model.Product
import co.uk.pencepoint.domain.repository.BasketRepository
import javax.inject.Inject

class AddToBasketUseCase @Inject constructor(
    private val basketRepository: BasketRepository
) {
    suspend operator fun invoke(product: Product, quantity: Int): Result<Unit> {
        return try {
            basketRepository.addToBasket(product, quantity)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}