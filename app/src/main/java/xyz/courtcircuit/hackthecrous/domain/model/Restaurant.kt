package xyz.courtcircuit.hackthecrous.domain.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import xyz.courtcircuit.hackthecrous.data.local.entity.MealEntity

data class Restaurant(
    val id: String,
    val name: String,
    val url: String,
    val hours: String,
    val gpsCoord: GpsCoord,
    val isFavorite: Boolean = false,
    val distance: Double? = null,
    val crowdLevel: CrowdLevel = CrowdLevel.UNKNOWN,
    val previewMeals: Flow<List<MealEntity>> = flow {
        emit(listOf(MealEntity(
            idMeal = "1",
            restaurantId = "1",
            typeMeal = "Déjeuner",
            foodiesJson = "{}"
        )))
    }

)

enum class CrowdLevel {
    LOW, MEDIUM, HIGH, UNKNOWN
}
