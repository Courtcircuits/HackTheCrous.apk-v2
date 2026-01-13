package xyz.courtcircuit.hackthecrous.data.remote.dto

import com.google.gson.annotations.SerializedName
import xyz.courtcircuit.hackthecrous.domain.model.Foodie
import xyz.courtcircuit.hackthecrous.domain.model.Meal

data class MealDto(
    @SerializedName("idmeal")
    val idMeal: Int,
    @SerializedName("typemeal")
    val typeMeal: String,
    @SerializedName("foodies")
    val foodies: List<FoodieDto>,
    @SerializedName("day")
    val day: String? = null,
    @SerializedName("idrestaurant")
    val idRestaurant: Int? = null
) {
    fun toDomain(): Meal {
        return Meal(
            idMeal = idMeal.toString(),
            typeMeal = typeMeal,
            foodies = foodies.map { it.toDomain() }
        )
    }
}

data class FoodieDto(
    @SerializedName("type")
    val type: String,
    @SerializedName("content")
    val content: List<String>
) {
    fun toDomain(): Foodie {
        return Foodie(
            type = type,
            content = content
        )
    }
}
