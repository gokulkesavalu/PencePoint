package co.uk.pencepoint.data.mapper

import co.uk.pencepoint.data.local.entities.BasketItemEntity
import co.uk.pencepoint.data.local.entities.TransactionEntity
import co.uk.pencepoint.domain.model.BasketItem
import co.uk.pencepoint.domain.model.Money
import co.uk.pencepoint.domain.model.Transaction
import co.uk.pencepoint.domain.model.TransactionStatus
import co.uk.pencepoint.domain.repository.TaxProvider


/**
 * Mappers for transaction-related data between data and domain layers.
 */

/**
 * Converts a [TransactionEntity] to its domain model [Transaction].
 *
 * @param taxProvider The [TaxProvider] used to map nested [BasketItemEntity] to [BasketItem].
 * @return A [Transaction] domain model.
 */
fun TransactionEntity.toDomain(taxProvider: TaxProvider) = Transaction(
    id = id,
    orderNumber = orderNumber,
    items = items.map { it.toDomainModel(taxProvider) },
    subTotalAmount = Money(subTotal),
    totalAmount = Money(total),
    taxAmount = Money(tax),
    status = TransactionStatus.valueOf(status),
    transactionReference = transactionReference,
    timestamp = timestamp,
)

/**
 * Converts a domain model [Transaction] to its Room entity [TransactionEntity].
 *
 * @return A [TransactionEntity] suitable for local storage.
 */
fun Transaction.toEntity() = TransactionEntity(
    id = id,
    orderNumber = orderNumber,
    items = items.map { it.toEntity() },
    subTotal = subTotalAmount.amountInPence,
    tax = taxAmount.amountInPence,
    total = totalAmount.amountInPence,
    status = status.name,
    transactionReference = transactionReference,
    timestamp = timestamp,
)
