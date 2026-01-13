package xyz.courtcircuit.hackthecrous.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import xyz.courtcircuit.hackthecrous.domain.model.Foodie
import xyz.courtcircuit.hackthecrous.domain.model.Meal

@Entity(tableName = "meals")
data class MealEntity(
    @PrimaryKey
    val idMeal: String,
    val restaurantId: String,
    val typeMeal: String,
    val foodiesJson: String,
    val lastUpdated: Long = System.currentTimeMillis()
) {
    fun toDomain(): Meal {
        val foodies: List<FoodieData> = Gson().fromJson(
            foodiesJson,
            object : TypeToken<List<FoodieData>>() {}.type
        )
        return Meal(
            idMeal = idMeal,
            typeMeal = typeMeal,
            foodies = foodies.map { it.toDomain() }
        )
    }

    companion object {
        fun fromDomain(meal: Meal, restaurantId: String): MealEntity {
            val foodiesData = meal.foodies.map { FoodieData.fromDomain(it) }
            return MealEntity(
                idMeal = meal.idMeal,
                restaurantId = restaurantId,
                typeMeal = meal.typeMeal,
                foodiesJson = Gson().toJson(foodiesData)
            )
        }
    }
}

data class FoodieData(
    val type: String,
    val content: List<String>
) {
    fun toDomain(): Foodie = Foodie(type, content)

    companion object {
        fun fromDomain(foodie: Foodie): FoodieData =
            FoodieData(foodie.type, foodie.content)
    }
}
