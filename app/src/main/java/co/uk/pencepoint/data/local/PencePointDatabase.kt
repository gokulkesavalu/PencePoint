package co.uk.pencepoint.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import co.uk.pencepoint.data.local.dao.BasketDao
import co.uk.pencepoint.data.local.dao.ProductDao
import co.uk.pencepoint.data.local.entities.BasketItemEntity
import co.uk.pencepoint.data.local.entities.ProductEntity
import co.uk.pencepoint.data.local.typeconverters.PencePointTypeConverters

/**
 * Main database for the PencePoint application.
 *
 * This database provides the local storage for products and other point-of-sale data,
 * enabling offline-first functionality.
 */
@Database(
    entities = [ProductEntity::class, BasketItemEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(PencePointTypeConverters::class)
abstract class PencePointDatabase : RoomDatabase() {
    /**
     * Returns the [ProductDao] to access product-related data.
     */
    abstract fun productDao(): ProductDao

    /**
     * Returns the [BasketDao] to access basket-related data.
     */
    abstract fun basketDao(): BasketDao
}