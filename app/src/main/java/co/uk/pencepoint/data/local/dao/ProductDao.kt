package co.uk.pencepoint.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import co.uk.pencepoint.data.local.entities.ProductEntity

/**
 * Data Access Object for the products table.
 */
@Dao
interface ProductDao {
    /**
     * Retrieves all products from the database.
     *
     * @return A list of all [ProductEntity] entries.
     */
    @Query("SELECT * FROM products")
    suspend fun getProducts(): List<ProductEntity>

    /**
     * Inserts a list of products into the database.
     * If a product already exists, it will be replaced.
     *
     * @param products The list of [ProductEntity] to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductEntity>)

    /**
     * Retrieves products filtered by category.
     *
     * @param category The category name to filter by.
     * @return A list of [ProductEntity] belonging to the specified category.
     */
    @Query("SELECT * FROM products WHERE category = :category")
    suspend fun getProductsByCategory(category: String): List<ProductEntity>
}