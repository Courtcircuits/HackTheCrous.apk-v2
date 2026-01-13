package xyz.courtcircuit.hackthecrous.presentation.restaurantdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import xyz.courtcircuit.hackthecrous.domain.model.Meal
import xyz.courtcircuit.hackthecrous.domain.model.Restaurant
import xyz.courtcircuit.hackthecrous.domain.repository.RestaurantRepository
import javax.inject.Inject

@HiltViewModel
class RestaurantDetailViewModel @Inject constructor(
    private val repository: RestaurantRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val restaurantId: String = checkNotNull(savedStateHandle["restaurantId"])

    private val _restaurant = MutableStateFlow<Restaurant?>(null)
    val restaurant: StateFlow<Restaurant?> = _restaurant.asStateFlow()

    private val _meals = MutableStateFlow<List<Meal>>(emptyList())
    val meals: StateFlow<List<Meal>> = _meals.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadRestaurantDetails()
        loadMeals()
    }

    private fun loadRestaurantDetails() {
        viewModelScope.launch {
            _restaurant.value = repository.getRestaurantById(restaurantId)
        }
    }

    private fun loadMeals() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.syncMeals(restaurantId)
                repository.getMealsByRestaurantId(restaurantId).collect { mealList ->
                    _meals.value = mealList
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            repository.toggleFavorite(restaurantId)
            loadRestaurantDetails()
        }
    }

    fun refresh() {
        loadMeals()
    }
}
