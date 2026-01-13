package xyz.courtcircuit.hackthecrous.domain.repository

import kotlinx.coroutines.flow.Flow
import xyz.courtcircuit.hackthecrous.domain.model.Meal
import xyz.courtcircuit.hackthecrous.domain.model.Restaurant

interface RestaurantRepository {
    fun getAllRestaurants(): Flow<List<Restaurant>>
    fun getFavoriteRestaurants(): Flow<List<Restaurant>>
    fun getRestaurantsWithPreviews(): Flow<List<Restaurant>>
    suspend fun getRestaurantById(id: String): Restaurant?
    suspend fun toggleFavorite(restaurantId: String)
    suspend fun searchRestaurant(query: String): Flow<List<Restaurant>>
    suspend fun syncRestaurants()
    fun getMealsByRestaurantId(restaurantId: String): Flow<List<Meal>>
    suspend fun syncMeals(restaurantId: String)
}
