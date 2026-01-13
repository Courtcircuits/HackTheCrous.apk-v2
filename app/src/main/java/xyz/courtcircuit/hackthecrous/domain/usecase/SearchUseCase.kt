package xyz.courtcircuit.hackthecrous.domain.usecase

import kotlinx.coroutines.flow.Flow
import xyz.courtcircuit.hackthecrous.domain.model.Restaurant
import xyz.courtcircuit.hackthecrous.domain.repository.RestaurantRepository
import javax.inject.Inject

class SearchUseCase @Inject constructor (
    private val repository: RestaurantRepository
){
    suspend operator fun invoke(query: String): Flow<List<Restaurant>> {
        return repository.searchRestaurant(query)
    }

}