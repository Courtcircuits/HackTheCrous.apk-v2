package xyz.courtcircuit.hackthecrous.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import xyz.courtcircuit.hackthecrous.data.local.dao.MealDao
import xyz.courtcircuit.hackthecrous.data.local.dao.RestaurantDao
import xyz.courtcircuit.hackthecrous.data.local.entity.MealEntity
import xyz.courtcircuit.hackthecrous.data.local.entity.RestaurantEntity

@Database(
    entities = [RestaurantEntity::class, MealEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun restaurantDao(): RestaurantDao
    abstract fun mealDao(): MealDao
}
