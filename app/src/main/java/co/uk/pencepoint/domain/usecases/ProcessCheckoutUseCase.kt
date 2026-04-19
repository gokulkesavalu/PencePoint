package co.uk.pencepoint.domain.usecases

import co.uk.pencepoint.domain.model.Money
import co.uk.pencepoint.domain.model.Transaction
import co.uk.pencepoint.domain.model.TransactionStatus
import co.uk.pencepoint.domain.repository.BasketRepository
import co.uk.pencepoint.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Use case to orchestrate the checkout process.
 *
 * This use case handles:
 * 1. Fetching the current basket items.
 * 2. Calculating the final totals.
 * 3. Generating order numbers and references.
 * 4. Saving the transaction to history.
 * 5. Clearing the shopping basket.
 */
class ProcessCheckoutUseCase @Inject constructor(
    private val basketRepository: BasketRepository,
    private val transactionRepository: TransactionRepository
) {
    /**
     * Executes the checkout process.
     *
     * @return A [Result] containing the completed [Transaction] if successful.
     */
    suspend operator fun invoke(): Result<Transaction> {
        return try {
            val basketItems = basketRepository.getBasketItems().first()
            if (basketItems.isEmpty()) {
                return Result.failure(Exception("Cannot checkout an empty basket"))
            }

            val subTotalPence = basketItems.sumOf { it.subtotal.amountInPence }
            val taxPence = basketItems.sumOf { it.taxAmount.amountInPence }
            val totalPence = subTotalPence + taxPence

            val transaction = Transaction(
                orderNumber = generateOrderNumber(),
                items = basketItems,
                subTotalAmount = Money(subTotalPence),
                taxAmount = Money(taxPence),
                totalAmount = Money(totalPence),
                timestamp = System.currentTimeMillis(),
                status = TransactionStatus.COMPLETED,
                transactionReference = generateTransactionReference()
            )

            transactionRepository.saveTransaction(transaction).getOrThrow()
            basketRepository.clearBasket().getOrThrow()
            Result.success(transaction)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun generateOrderNumber(): Long {
        return (10_000_000L..99_999_999L).random()
    }

    private fun generateTransactionReference(): String {
        val allowedChars = ('A'..'Z') + ('0'..'9')
        return (1..10)
            .map { allowedChars.random() }
            .joinToString("")
    }
}
