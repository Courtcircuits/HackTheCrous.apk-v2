package xyz.courtcircuit.hackthecrous.domain.usecase

import kotlinx.coroutines.flow.Flow
import xyz.courtcircuit.hackthecrous.domain.model.Restaurant
import xyz.courtcircuit.hackthecrous.domain.repository.RestaurantRepository
import javax.inject.Inject

class GetRestaurantsUseCase @Inject constructor(
    private val repository: RestaurantRepository
) {
    operator fun invoke(): Flow<List<Restaurant>> {
        return repository.getRestaurantsWithPreviews()
    }
}
