package co.uk.pencepoint.di

import co.uk.pencepoint.data.repository.LocalTaxProvider
import co.uk.pencepoint.domain.repository.TaxProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module that provides application-level dependencies.
 *
 * This module is installed in the [SingletonComponent], meaning the provided
 * dependencies will have the same lifecycle as the application.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    /**
     * Binds the [LocalTaxProvider] implementation to the [TaxProvider] interface.
     */
    @Binds
    @Singleton
    abstract fun bindTaxProvider(impl: LocalTaxProvider): TaxProvider
}
