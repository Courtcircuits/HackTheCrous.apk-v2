package xyz.courtcircuit.hackthecrous.data.remote.dto

import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.flow.Flow
import xyz.courtcircuit.hackthecrous.data.local.entity.MealEntity
import xyz.courtcircuit.hackthecrous.domain.model.GpsCoord
import xyz.courtcircuit.hackthecrous.domain.model.Meal
import xyz.courtcircuit.hackthecrous.domain.model.Restaurant

data class RestaurantResponse(
    @SerializedName("data")
    val data: List<RestaurantDto>
)

data class RestaurantDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("attributes")
    val attributes: RestaurantAttributes
) {
    fun toDomain(): Restaurant {
        return Restaurant(
            id = id,
            name = attributes.name,
            url = attributes.url,
            hours = attributes.hours,
            gpsCoord = GpsCoord(
                latitude = attributes.gpsCoord.x,
                longitude = attributes.gpsCoord.y
            )
        )
    }

    fun toDomainWithMeals(previewMeals: Flow<List<MealEntity>>): Restaurant {
        return Restaurant(
            id = id,
            name = attributes.name,
            url = attributes.url,
            hours = attributes.hours,
            gpsCoord = GpsCoord(
                latitude = attributes.gpsCoord.x,
                longitude = attributes.gpsCoord.y
            ),
            previewMeals = previewMeals
        )
    }
}

data class RestaurantAttributes(
    @SerializedName("url")
    val url: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("hours")
    val hours: String,
    @SerializedName("gps_coord")
    val gpsCoord: GpsCoordDto
)

data class GpsCoordDto(
    @SerializedName("X")
    val x: Double,
    @SerializedName("Y")
    val y: Double
)
