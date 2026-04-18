package co.uk.pencepoint.di

import co.uk.pencepoint.data.local.PencePointDatabase
import co.uk.pencepoint.data.local.dao.ProductDao
import co.uk.pencepoint.data.repository.LocalTaxProvider
import co.uk.pencepoint.data.repository.ProductRepositoryImpl
import co.uk.pencepoint.domain.repository.ProductRepository
import co.uk.pencepoint.domain.repository.TaxProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module that provides repository-related dependencies.
 *
 * This module binds repository interfaces to their concrete implementations.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    /**
     * Binds the [ProductRepositoryImpl] to the [ProductRepository] interface.
     */
    @Binds
    @Singleton
    abstract fun bindProductRepository(
        productRepositoryImpl: ProductRepositoryImpl
    ): ProductRepository
}
