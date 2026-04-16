package co.uk.pencepoint.di

import co.uk.pencepoint.data.repository.LocalTaxProvider
import co.uk.pencepoint.domain.repository.TaxProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindTaxProvider(impl: LocalTaxProvider): TaxProvider
}
