package xyz.courtcircuit.hackthecrous.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import xyz.courtcircuit.hackthecrous.data.local.entity.RestaurantEntity

@Dao
interface RestaurantDao {
    @Query("SELECT * FROM restaurants")
    fun getAllRestaurants(): Flow<List<RestaurantEntity>>

    @Query("SELECT * FROM restaurants WHERE id = :id")
    suspend fun getRestaurantById(id: String): RestaurantEntity?

    @Query("SELECT * FROM restaurants WHERE isFavorite = 1")
    fun getFavoriteRestaurants(): Flow<List<RestaurantEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRestaurants(restaurants: List<RestaurantEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRestaurant(restaurant: RestaurantEntity)

    @Update
    suspend fun updateRestaurant(restaurant: RestaurantEntity)

    @Query("UPDATE restaurants SET isFavorite = :isFavorite WHERE id = :restaurantId")
    suspend fun updateFavoriteStatus(restaurantId: String, isFavorite: Boolean)

    @Query("DELETE FROM restaurants")
    suspend fun deleteAllRestaurants()
}
