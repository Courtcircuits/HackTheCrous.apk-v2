package xyz.courtcircuit.hackthecrous.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.coroutines.flow.Flow
import xyz.courtcircuit.hackthecrous.domain.model.CrowdLevel
import xyz.courtcircuit.hackthecrous.domain.model.GpsCoord
import xyz.courtcircuit.hackthecrous.domain.model.Restaurant

@Entity(tableName = "restaurants")
data class RestaurantEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val url: String,
    val hours: String,
    val latitude: Double,
    val longitude: Double,
    val isFavorite: Boolean = false,
    val lastUpdated: Long = System.currentTimeMillis()
) {
    fun toDomain(): Restaurant {
        return Restaurant(
            id = id,
            name = name,
            url = url,
            hours = hours,
            gpsCoord = GpsCoord(latitude, longitude),
            isFavorite = isFavorite,
            crowdLevel = CrowdLevel.UNKNOWN
        )
    }

    fun toDomainWithMeals(previewMeals: Flow<List<MealEntity>>): Restaurant {
        return Restaurant(
            id = id,
            name = name,
            url = url,
            hours = hours,
            gpsCoord = GpsCoord(latitude, longitude),
            isFavorite = isFavorite,
            previewMeals = previewMeals
        )
    }

    companion object {
        fun fromDomain(restaurant: Restaurant): RestaurantEntity {
            return RestaurantEntity(
                id = restaurant.id,
                name = restaurant.name,
                url = restaurant.url,
                hours = restaurant.hours,
                latitude = restaurant.gpsCoord.latitude,
                longitude = restaurant.gpsCoord.longitude,
                isFavorite = restaurant.isFavorite
            )
        }
    }
}
