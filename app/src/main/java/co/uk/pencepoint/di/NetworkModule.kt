package co.uk.pencepoint.di

import co.uk.pencepoint.BuildConfig
import co.uk.pencepoint.data.remote.FakeStoreApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Hilt module that provides network-related dependencies.
 *
 * This module is responsible for configuring Retrofit, OkHttp, and the API services.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * Provides an [HttpLoggingInterceptor] for logging network requests and responses.
     * Logging is only enabled for debug builds.
     */
    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    /**
     * Provides an [OkHttpClient] configured with the logging interceptor.
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    /**
     * Provides an instance of [FakeStoreApi] using Retrofit.
     */
    @Provides
    @Singleton
    fun provideFakeStoreApi(okHttpClient: OkHttpClient): FakeStoreApi {
        return Retrofit.Builder()
            .baseUrl(FakeStoreApi.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FakeStoreApi::class.java)
    }
}
