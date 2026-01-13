package xyz.courtcircuit.hackthecrous.presentation.restaurant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import xyz.courtcircuit.hackthecrous.domain.model.Restaurant
import xyz.courtcircuit.hackthecrous.domain.model.RestaurantType
import xyz.courtcircuit.hackthecrous.domain.usecase.GetMealsUseCase
import xyz.courtcircuit.hackthecrous.domain.usecase.GetRestaurantsUseCase
import xyz.courtcircuit.hackthecrous.domain.usecase.SyncRestaurantsUseCase
import xyz.courtcircuit.hackthecrous.domain.usecase.ToggleFavoriteUseCase
import javax.inject.Inject

@HiltViewModel
class RestaurantViewModel @Inject constructor(
    private val getRestaurantsUseCase: GetRestaurantsUseCase,
    private val syncRestaurantsUseCase: SyncRestaurantsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val getMealsUseCase: GetMealsUseCase
) : ViewModel() {

    private val _selectedTab = MutableStateFlow(RestaurantType.ALL)
    val selectedTab: StateFlow<RestaurantType> = _selectedTab

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    val restaurants: StateFlow<List<Restaurant>> = combine(
        getRestaurantsUseCase(),
        _selectedTab
    ) { restaurants, selectedTab ->
        filterRestaurants(restaurants, selectedTab)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        syncRestaurants()
    }

    fun selectTab(tab: RestaurantType) {
        _selectedTab.value = tab
    }

    fun toggleFavorite(restaurantId: String) {
        viewModelScope.launch {
            toggleFavoriteUseCase(restaurantId)
        }
    }

    fun syncRestaurants() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                syncRestaurantsUseCase()
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun filterRestaurants(
        restaurants: List<Restaurant>,
        type: RestaurantType
    ): List<Restaurant> {
        if (type == RestaurantType.ALL) return restaurants

        return restaurants.filter { restaurant ->
            RestaurantType.fromRestaurantName(restaurant.name) == type
        }
    }
}
