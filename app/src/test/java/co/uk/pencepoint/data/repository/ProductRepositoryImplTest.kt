package co.uk.pencepoint.data.repository

import co.uk.pencepoint.data.local.dao.ProductDao
import co.uk.pencepoint.data.local.entities.ProductEntity
import co.uk.pencepoint.data.local.entities.RatingEntity
import co.uk.pencepoint.data.remote.FakeStoreApi
import co.uk.pencepoint.data.remote.dto.ProductDto
import co.uk.pencepoint.data.remote.dto.RatingDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.concurrent.TimeUnit

/**
 * Unit tests for [ProductRepositoryImpl].
 */
class ProductRepositoryImplTest {
    private val api: FakeStoreApi = mockk()
    private val productDao: ProductDao = mockk(relaxed = true)
    private val taxProvider: LocalTaxProvider = mockk()
    private val repository: ProductRepositoryImpl =
        ProductRepositoryImpl(api, productDao, taxProvider)

    private val sampleDto = ProductDto(
        id = 1,
        title = "Test Product",
        price = 10.0,
        description = "Description",
        category = "electronics",
        image = "url",
        rating = RatingDto(4.5, 100)
    )

    private val sampleEntity = ProductEntity(
        id = 1,
        title = "Test Product",
        price = 10.0,
        description = "Description",
        category = "electronics",
        image = "url",
        rating = RatingEntity(4.5, 100),
        cachedAt = System.currentTimeMillis()
    )

    @Before
    fun setUp() {
        every { taxProvider.getTaxRate(any(), any()) } returns 15.0
    }

    @Test
    fun `getProducts returns cached data when cache is valid`() = runTest {
        val validCache = listOf(sampleEntity.copy(cachedAt = System.currentTimeMillis()))
        coEvery { productDao.getProducts() } returns validCache

        val result = repository.getProducts()

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull()?.size)
        assertEquals("Test Product", result.getOrNull()?.first()?.title)
        coVerify(exactly = 0) { api.getProducts() }
        coVerify(exactly = 0) { productDao.insertProducts(any()) }
    }

    @Test
    fun `getProducts fetches from API when cache is empty`() = runTest {
        coEvery { productDao.getProducts() } returns emptyList()
        coEvery { api.getProducts() } returns listOf(sampleDto)

        val result = repository.getProducts()

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull()?.size)
        coVerify(exactly = 1) { api.getProducts() }
        coVerify(exactly = 1) { productDao.insertProducts(any()) }
    }

    @Test
    fun `getProducts fetches from API when cache is expired`() = runTest {
        val expiredTimestamp = System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(11)
        val expiredCache = listOf(sampleEntity.copy(cachedAt = expiredTimestamp))
        coEvery { productDao.getProducts() } returns expiredCache
        coEvery { api.getProducts() } returns listOf(sampleDto)

        val result = repository.getProducts()

        assertTrue(result.isSuccess)
        coVerify(exactly = 1) { api.getProducts() }
        coVerify(exactly = 1) { productDao.insertProducts(any()) }
    }

    @Test
    fun `getProducts returns cached data when API fails`() = runTest {
        val expiredTimestamp = System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(11)
        val cachedData = listOf(sampleEntity.copy(cachedAt = expiredTimestamp))
        coEvery { productDao.getProducts() } returns cachedData
        coEvery { api.getProducts() } throws Exception("Network Error")

        val result = repository.getProducts()

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull()?.size)
        coVerify(exactly = 1) { api.getProducts() }
        coVerify(exactly = 0) { productDao.insertProducts(any()) }
    }
}
