package co.uk.pencepoint.data.remote

import co.uk.pencepoint.data.remote.dto.ProductDto
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Retrofit interface for the FakeStore API.
 * Provides endpoints for fetching product data.
 */
interface FakeStoreApi {

    /**
     * Fetches all products from the API.
     *
     * @return A list of [ProductDto] objects.
     */
    @GET("products")
    suspend fun getProducts(): List<ProductDto>

    /**
     * Fetches products filtered by a specific category.
     *
     * @param category The category name to filter by.
     * @return A list of [ProductDto] objects belonging to the category.
     */
    @GET("products/category/{category}")
    suspend fun getProducts(@Path("category") category: String): List<ProductDto>

    companion object {
        const val BASE_URL = "https://fakestoreapi.com/"
    }
}
