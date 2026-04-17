package co.uk.pencepoint.data.local.typeconverters

import androidx.room.TypeConverter
import co.uk.pencepoint.data.local.entities.RatingEntity
import com.google.gson.Gson

class PencePointTypeConverters {
    private val gson = Gson()


    @TypeConverter
    fun fromProductRating(rating: RatingEntity): String {
        return gson.toJson(rating)
    }

    @TypeConverter
    fun toProductRating(ratingString: String): RatingEntity {
        return gson.fromJson(ratingString, RatingEntity::class.java)
    }
}