package xyz.courtcircuit.hackthecrous.data.remote.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import xyz.courtcircuit.hackthecrous.data.remote.dto.MealDto
import xyz.courtcircuit.hackthecrous.data.remote.dto.RestaurantResponse

interface HackTheCrousApi {
    @GET("v2/restaurants/")
    suspend fun getAllRestaurants(): RestaurantResponse

    @GET("v2/restaurants/{id}")
    suspend fun getRestaurantById(@Path("id") id: String): RestaurantResponse

    @GET("v2/restaurants/search")
    suspend fun searchRestaurants(@Query("q") query: String): RestaurantResponse

    @GET("v2/restaurants/meals/{id}")
    suspend fun getMealsByRestaurantId(@Path("id") restaurantId: String): List<MealDto>

    @GET("v2/restaurants/schools/{id}")
    suspend fun getRestaurantsBySchool(@Path("id") schoolId: String): RestaurantResponse
}
