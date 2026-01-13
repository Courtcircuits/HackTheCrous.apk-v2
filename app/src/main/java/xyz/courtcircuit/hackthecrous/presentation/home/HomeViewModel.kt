package xyz.courtcircuit.hackthecrous.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import xyz.courtcircuit.hackthecrous.domain.model.Meal
import xyz.courtcircuit.hackthecrous.domain.model.Restaurant
import xyz.courtcircuit.hackthecrous.domain.repository.RestaurantRepository
import xyz.courtcircuit.hackthecrous.domain.usecase.SyncRestaurantsUseCase
import xyz.courtcircuit.hackthecrous.domain.usecase.ToggleFavoriteUseCase
import javax.inject.Inject

data class RestaurantWithMeals(
    val restaurant: Restaurant,
    val meals: List<Meal>
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: RestaurantRepository,
    private val syncRestaurantsUseCase: SyncRestaurantsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    val favoriteRestaurantsWithMeals: StateFlow<List<RestaurantWithMeals>> =
        repository.getFavoriteRestaurants()
            .map { restaurants ->
                restaurants.map { restaurant ->
                    val meals = repository.getMealsByRestaurantId(restaurant.id).first()
                    RestaurantWithMeals(restaurant, meals)
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    init {
        syncRestaurants()
    }

    fun toggleFavorite(restaurantId: String) {
        viewModelScope.launch {
            toggleFavoriteUseCase(restaurantId)
        }
    }

    private fun syncRestaurants() {
        viewModelScope.launch {
            syncRestaurantsUseCase()
        }
    }
}
