package xyz.courtcircuit.hackthecrous.data.repository

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import xyz.courtcircuit.hackthecrous.data.local.dao.MealDao
import xyz.courtcircuit.hackthecrous.data.local.dao.RestaurantDao
import xyz.courtcircuit.hackthecrous.data.local.entity.MealEntity
import xyz.courtcircuit.hackthecrous.data.local.entity.RestaurantEntity
import xyz.courtcircuit.hackthecrous.data.remote.api.HackTheCrousApi
import xyz.courtcircuit.hackthecrous.domain.model.Meal
import xyz.courtcircuit.hackthecrous.domain.model.Restaurant
import xyz.courtcircuit.hackthecrous.domain.repository.RestaurantRepository
import javax.inject.Inject

class RestaurantRepositoryImpl @Inject constructor(
    private val restaurantDao: RestaurantDao,
    private val mealDao: MealDao,
    private val api: HackTheCrousApi
) : RestaurantRepository {

    override fun getAllRestaurants(): Flow<List<Restaurant>> {
        return restaurantDao.getAllRestaurants()
            .map { entities -> entities.map { it.toDomain() } }
    }

    override fun getRestaurantsWithPreviews(): Flow<List<Restaurant>> {
        return restaurantDao.getAllRestaurants()
            .map { entities -> entities.map{
                val previewMeals = mealDao.getMealsByRestaurantId(it.id)
                it.toDomainWithMeals(previewMeals)
            } }
    }


    override fun getFavoriteRestaurants(): Flow<List<Restaurant>> {
        return restaurantDao.getFavoriteRestaurants()
            .map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun getRestaurantById(id: String): Restaurant? {
        return restaurantDao.getRestaurantById(id)?.toDomain()
    }

    override suspend fun toggleFavorite(restaurantId: String) {
        val restaurant = restaurantDao.getRestaurantById(restaurantId)
        restaurant?.let {
            restaurantDao.updateFavoriteStatus(restaurantId, !it.isFavorite)
        }
    }

    override suspend fun searchRestaurant(query: String): Flow<List<Restaurant>> {
        val results = api.searchRestaurants(query)

        // Sync meals for each restaurant in the search results
        results.data.forEach { restaurantDto ->
            try {
                syncMeals(restaurantDto.id)
            } catch (e: Exception) {
                Log.e("RestaurantRepo", "Error syncing meals for searched restaurant ${restaurantDto.id}", e)
            }
        }

        return flow{ emit(results.data.map { it.toDomain() })}
    }

    override suspend fun syncRestaurants() {
        try {
            Log.d("RestaurantRepo", "Syncing restaurants from API")
            val response = api.getAllRestaurants()
            Log.d("RestaurantRepo", "Fetched ${response.data.size} restaurants from API")
            val entities = response.data.map { dto ->
                val existingRestaurant = restaurantDao.getRestaurantById(dto.id)
                RestaurantEntity(
                    id = dto.id,
                    name = dto.attributes.name,
                    url = dto.attributes.url,
                    hours = dto.attributes.hours,
                    latitude = dto.attributes.gpsCoord.x,
                    longitude = dto.attributes.gpsCoord.y,
                    isFavorite = existingRestaurant?.isFavorite ?: false
                )
            }
            restaurantDao.insertRestaurants(entities)
            Log.d("RestaurantRepo", "Successfully saved ${entities.size} restaurants to database")

            // Sync meals for each restaurant
            entities.forEach { restaurant ->
                try {
                    syncMeals(restaurant.id)
                } catch (e: Exception) {
                    Log.e("RestaurantRepo", "Error syncing meals for restaurant ${restaurant.id}", e)
                    // Continue with other restaurants even if one fails
                }
            }
        } catch (e: Exception) {
            Log.e("RestaurantRepo", "Error syncing restaurants", e)
            // Silently fail for local-first approach
            // The app will continue to use cached data
        }
    }

    override fun getMealsByRestaurantId(restaurantId: String): Flow<List<Meal>> {
        return mealDao.getMealsByRestaurantId(restaurantId)
            .map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun syncMeals(restaurantId: String) {
        try {
            Log.d("RestaurantRepo", "Syncing meals for restaurant: $restaurantId")
            val meals = api.getMealsByRestaurantId(restaurantId)
            Log.d("RestaurantRepo", "Fetched ${meals.size} meals from API")
            val entities = meals.map { dto ->
                MealEntity.fromDomain(dto.toDomain(), restaurantId)
            }
            mealDao.deleteMealsByRestaurantId(restaurantId)
            mealDao.insertMeals(entities)
            Log.d("RestaurantRepo", "Successfully saved ${entities.size} meals to database")
        } catch (e: Exception) {
            Log.e("RestaurantRepo", "Error syncing meals for restaurant $restaurantId", e)
            // Silently fail for local-first approach
        }
    }
}

