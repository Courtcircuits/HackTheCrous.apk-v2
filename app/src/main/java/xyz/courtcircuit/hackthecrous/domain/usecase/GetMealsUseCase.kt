package xyz.courtcircuit.hackthecrous.domain.usecase

import kotlinx.coroutines.flow.Flow
import xyz.courtcircuit.hackthecrous.domain.model.Meal
import xyz.courtcircuit.hackthecrous.domain.repository.RestaurantRepository
import javax.inject.Inject

class GetMealsUseCase @Inject constructor(
    private val repository: RestaurantRepository
) {
    operator fun invoke(restaurantId: String): Flow<List<Meal>> {
        return repository.getMealsByRestaurantId(restaurantId)
    }
}
