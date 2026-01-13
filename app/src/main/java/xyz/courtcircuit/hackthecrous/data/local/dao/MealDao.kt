package xyz.courtcircuit.hackthecrous.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import xyz.courtcircuit.hackthecrous.data.local.entity.MealEntity

@Dao
interface MealDao {
    @Query("SELECT * FROM meals WHERE restaurantId = :restaurantId")
    fun getMealsByRestaurantId(restaurantId: String): Flow<List<MealEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeals(meals: List<MealEntity>)

    @Query("DELETE FROM meals WHERE restaurantId = :restaurantId")
    suspend fun deleteMealsByRestaurantId(restaurantId: String)
}
