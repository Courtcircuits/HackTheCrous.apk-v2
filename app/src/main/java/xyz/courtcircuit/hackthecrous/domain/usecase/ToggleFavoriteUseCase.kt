package xyz.courtcircuit.hackthecrous.domain.usecase

import xyz.courtcircuit.hackthecrous.domain.repository.RestaurantRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val repository: RestaurantRepository
) {
    suspend operator fun invoke(restaurantId: String) {
        repository.toggleFavorite(restaurantId)
    }
}
