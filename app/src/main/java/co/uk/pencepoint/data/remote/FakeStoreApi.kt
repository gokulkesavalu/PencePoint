package co.uk.pencepoint.data.remote

import co.uk.pencepoint.data.remote.dto.ProductDto
import retrofit2.http.GET
import retrofit2.http.Path

interface FakeStoreApi {

    @GET("products")
    suspend fun getProducts(): List<ProductDto>

    @GET("products/category/{category}")
    suspend fun getProducts(@Path("category") category: String): List<ProductDto>

    companion object {
        const val BASE_URL = "https://fakestoreapi.com/"
    }
}
