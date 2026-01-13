package xyz.courtcircuit.hackthecrous.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import xyz.courtcircuit.hackthecrous.domain.model.Meal
import xyz.courtcircuit.hackthecrous.domain.model.Restaurant
import xyz.courtcircuit.hackthecrous.domain.repository.RestaurantRepository
import xyz.courtcircuit.hackthecrous.domain.usecase.SearchUseCase
import javax.inject.Inject

data class SearchResult(
    val restaurant: Restaurant,
    val matchedMeals: List<Meal>
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase,
    private val repository: RestaurantRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<SearchResult>>(emptyList())
    val searchResults: StateFlow<List<SearchResult>> = _searchResults.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        if (query.isBlank()) {
            _searchResults.value = emptyList()
            _isSearching.value = false
        } else {
            searchMeals(query)
        }
    }

    private fun searchMeals(query: String) {
        viewModelScope.launch {
            _isSearching.value = true
            try {
                searchUseCase(query)
                    .catch { e ->
                        _isSearching.value = false
                        _searchResults.value = emptyList()
                    }
                    .collectLatest { restaurants ->
                        // Process all restaurants concurrently
                        val results = restaurants.map { restaurant ->
                            async {
                                try {
                                    // Get meals for this restaurant
                                    val meals = repository.getMealsByRestaurantId(restaurant.id)
                                        .first()

                                    // Filter meals that contain the search query
                                    val matchedMeals = meals.filter { meal ->
                                        meal.foodies.any { foodie ->
                                            foodie.content.any { content ->
                                                content.contains(query, ignoreCase = true)
                                            }
                                        }
                                    }

                                    if (matchedMeals.isNotEmpty()) {
                                        SearchResult(restaurant, matchedMeals)
                                    } else null
                                } catch (e: Exception) {
                                    null
                                }
                            }
                        }.awaitAll().filterNotNull()

                        _searchResults.value = results
                        _isSearching.value = false
                    }
            } catch (e: Exception) {
                _isSearching.value = false
                _searchResults.value = emptyList()
            }
        }
    }

    fun clearSearch() {
        _searchQuery.value = ""
        _searchResults.value = emptyList()
        _isSearching.value = false
    }
}
