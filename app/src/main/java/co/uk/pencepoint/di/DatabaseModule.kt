package co.uk.pencepoint.di

import android.content.Context
import androidx.room.Room
import co.uk.pencepoint.data.local.PencePointDatabase
import co.uk.pencepoint.data.local.dao.BasketDao
import co.uk.pencepoint.data.local.dao.ProductDao
import co.uk.pencepoint.data.local.dao.TransactionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for providing database-related dependencies.
 * This includes the Room database instance and its DAOs.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * Provides the singleton [PencePointDatabase] instance.
     *
     * @param app The application context.
     * @return The initialized [PencePointDatabase].
     */
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext app: Context): PencePointDatabase {
        return Room.databaseBuilder(
            app,
            PencePointDatabase::class.java,
            "pence_point_database"
        ).build()
    }

    /**
     * Provides the [ProductDao] for database operations.
     *
     * @param appDatabase The [PencePointDatabase] instance.
     * @return The [ProductDao] instance.
     */
    @Provides
    @Singleton
    fun provideProductDao(appDatabase: PencePointDatabase) = appDatabase.productDao()

    /**
     * Provides the [BasketDao] for basket-related operations.
     *
     * @param appDatabase The [PencePointDatabase] instance.
     * @return The [BasketDao] instance.
     */
    @Provides
    @Singleton
    fun provideBasketDao(appDatabase: PencePointDatabase) = appDatabase.basketDao()

    /**
     * Provides the [TransactionDao] for transaction-related operations.
     *
     * @param appDatabase The [PencePointDatabase] instance.
     * @return The [TransactionDao] instance.
     */
    @Provides
    @Singleton
    fun provideTransactionDao(appDatabase: PencePointDatabase) = appDatabase.transactionDao()
}