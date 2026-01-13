package xyz.courtcircuit.hackthecrous.presentation.restaurant

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.map
import xyz.courtcircuit.hackthecrous.domain.model.GpsCoord
import xyz.courtcircuit.hackthecrous.domain.model.Restaurant
import xyz.courtcircuit.hackthecrous.domain.model.RestaurantType
import xyz.courtcircuit.hackthecrous.presentation.restaurant.components.RestaurantCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantScreen(
    onRestaurantClick: (String) -> Unit,
    viewModel: RestaurantViewModel = hiltViewModel()
) {
    val restaurants by viewModel.restaurants.collectAsState()
    val selectedTab by viewModel.selectedTab.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    RestaurantScreenContent(
        restaurants = restaurants,
        selectedTab = selectedTab,
        isLoading = isLoading,
        onRestaurantClick = onRestaurantClick,
        onTabSelected = viewModel::selectTab,
        onFavoriteClick = viewModel::toggleFavorite,
        onRefresh = viewModel::syncRestaurants
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantScreenContent(
    restaurants: List<xyz.courtcircuit.hackthecrous.domain.model.Restaurant>,
    selectedTab: RestaurantType,
    isLoading: Boolean,
    onRestaurantClick: (String) -> Unit,
    onTabSelected: (RestaurantType) -> Unit,
    onFavoriteClick: (String) -> Unit,
    onRefresh: () -> Unit
) {
    val tabs = listOf(
        "Tout" to RestaurantType.ALL,
        "Restos" to RestaurantType.RESTO,
        "Cafet'" to RestaurantType.CAFET,
        "Brasseries" to RestaurantType.BRASSERIE
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1C1C1C))
    ) {
        TabRow(
            selectedTabIndex = tabs.indexOfFirst { it.second == selectedTab },
            containerColor = Color(0xFF1C1C1C),
            contentColor = Color.White,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[tabs.indexOfFirst { it.second == selectedTab }]),
                    color = Color(0xFF00C853)
                )
            }
        ) {
            tabs.forEach { (title, type) ->
                Tab(
                    selected = selectedTab == type,
                    onClick = { onTabSelected(type) },
                    text = {
                        Text(
                            text = title,
                            color = if (selectedTab == type) Color.White else Color.Gray,
                            fontWeight = if (selectedTab == type) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }
        }

        PullToRefreshBox(
            isRefreshing = isLoading,
            onRefresh = onRefresh,
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    items = restaurants,
                    key = { it.id }
                ) { restaurant ->
                    val previews by restaurant.previewMeals.collectAsState(listOf())

                    RestaurantCard(
                        restaurant = restaurant,
                        onClick = { onRestaurantClick(restaurant.id) },
                        onFavoriteClick = onFavoriteClick,
                        previewMeals = previews.flatMap { meal ->
                            meal.toDomain().foodies.flatMap { foodie ->
                                foodie.content.filter { it.isNotBlank() && it != "***" }
                            }
                        }.take(5)
                    )
                }
            }
        }
    }
}

// Preview data
private val mockRestaurants = listOf(
    Restaurant(
        id = "1",
        name = "Brasserie Boutonnet",
        url = "https://example.com",
        hours = "07:30 - 22:00",
        gpsCoord = GpsCoord(43.623478, 3.869285),
        isFavorite = true
    ),
    Restaurant(
        id = "2",
        name = "Restaurant de la street",
        url = "https://example.com",
        hours = "11:30 - 14:00",
        gpsCoord = GpsCoord(43.631014, 3.860346),
        isFavorite = true
    ),
    Restaurant(
        id = "3",
        name = "Toi même tu connais",
        url = "https://example.com",
        hours = "08:00 - 21:00",
        gpsCoord = GpsCoord(43.6349531, 3.870764),
        isFavorite = false
    ),
    Restaurant(
        id = "4",
        name = "J'vais en ambiance mondaine",
        url = "https://example.com",
        hours = "07:30 - 18:00",
        gpsCoord = GpsCoord(43.614331, 3.876551),
        isFavorite = false
    )
)

@Preview(showBackground = true)
@Composable
fun RestaurantScreenPreview() {
    xyz.courtcircuit.hackthecrous.ui.theme.HackthecrousTheme {
        RestaurantScreenContent(
            restaurants = mockRestaurants,
            selectedTab = RestaurantType.ALL,
            isLoading = false,
            onRestaurantClick = {},
            onTabSelected = {},
            onFavoriteClick = {},
            onRefresh = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RestaurantScreenLoadingPreview() {
    xyz.courtcircuit.hackthecrous.ui.theme.HackthecrousTheme {
        RestaurantScreenContent(
            restaurants = mockRestaurants,
            selectedTab = RestaurantType.CAFET,
            isLoading = true,
            onRestaurantClick = {},
            onTabSelected = {},
            onFavoriteClick = {},
            onRefresh = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RestaurantScreenEmptyPreview() {
    xyz.courtcircuit.hackthecrous.ui.theme.HackthecrousTheme {
        RestaurantScreenContent(
            restaurants = emptyList(),
            selectedTab = RestaurantType.ALL,
            isLoading = false,
            onRestaurantClick = {},
            onTabSelected = {},
            onFavoriteClick = {},
            onRefresh = {}
        )
    }
}