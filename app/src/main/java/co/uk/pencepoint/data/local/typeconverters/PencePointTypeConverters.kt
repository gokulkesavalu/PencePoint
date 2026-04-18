package co.uk.pencepoint.data.local.typeconverters

import androidx.room.TypeConverter
import co.uk.pencepoint.data.local.entities.ProductEntity
import co.uk.pencepoint.data.local.entities.RatingEntity
import com.google.gson.Gson

/**
 * Room type converters for the PencePoint application.
 *
 * These converters allow Room to store complex objects like [RatingEntity] and [ProductEntity]
 * by serializing them to and from JSON strings using Gson.
 */
class PencePointTypeConverters {
    private val gson = Gson()

    /**
     * Converts a [RatingEntity] to a JSON string.
     */
    @TypeConverter
    fun fromProductRating(rating: RatingEntity): String {
        return gson.toJson(rating)
    }

    /**
     * Converts a JSON string back to a [RatingEntity].
     */
    @TypeConverter
    fun toProductRating(ratingString: String): RatingEntity {
        return gson.fromJson(ratingString, RatingEntity::class.java)
    }

    /**
     * Converts a [ProductEntity] to a JSON string.
     */
    @TypeConverter
    fun fromProductEntity(product: ProductEntity): String {
        return gson.toJson(product)
    }

    /**
     * Converts a JSON string back to a [ProductEntity].
     */
    @TypeConverter
    fun toProductEntity(productString: String): ProductEntity {
        return gson.fromJson(productString, ProductEntity::class.java)
    }
}