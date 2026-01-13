package xyz.courtcircuit.hackthecrous.domain.usecase

import xyz.courtcircuit.hackthecrous.domain.repository.RestaurantRepository
import javax.inject.Inject

class SyncRestaurantsUseCase @Inject constructor(
    private val repository: RestaurantRepository
) {
    suspend operator fun invoke() {
        repository.syncRestaurants()
    }
}
