package xyz.courtcircuit.hackthecrous.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import xyz.courtcircuit.hackthecrous.domain.model.CrowdLevel
import xyz.courtcircuit.hackthecrous.domain.model.Foodie
import xyz.courtcircuit.hackthecrous.domain.model.GpsCoord
import xyz.courtcircuit.hackthecrous.domain.model.Meal
import xyz.courtcircuit.hackthecrous.domain.model.Restaurant
import xyz.courtcircuit.hackthecrous.presentation.home.components.FavoriteRestaurantCard

@Composable
fun HomeScreen(
    onRestaurantClick: (String) -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    val favoriteRestaurantsWithMeals by viewModel.favoriteRestaurantsWithMeals.collectAsState()

    HomeScreenContent(
        favoriteRestaurantsWithMeals = favoriteRestaurantsWithMeals,
        onRestaurantClick = onRestaurantClick,
        onFavoriteClick = viewModel::toggleFavorite
    )
}

@Composable
fun HomeScreenContent(
    favoriteRestaurantsWithMeals: List<RestaurantWithMeals>,
    onRestaurantClick: (String) -> Unit,
    onFavoriteClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1C1C1C))
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Hello my friend 👋",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "À la carte de ton resto préféré",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(
                items = favoriteRestaurantsWithMeals,
                key = { it.restaurant.id }
            ) { restaurantWithMeals ->
                FavoriteRestaurantCard(
                    restaurant = restaurantWithMeals.restaurant,
                    meals = restaurantWithMeals.meals,
                    onClick = { onRestaurantClick(restaurantWithMeals.restaurant.id) },
                    onFavoriteClick = onFavoriteClick,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }
    }
}

private val mockMeals = listOf(
    Meal(
        idMeal = "1",
        typeMeal = "Petit-déjeuner",
        foodies = listOf(
            Foodie(
                type = "Plat",
                content = listOf("Frites", "Raviolis aux aubergines")
            )
        )
    ),
    Meal(
        idMeal = "2",
        typeMeal = "Déjeuner",
        foodies = listOf(
            Foodie(
                type = "Plat",
                content = listOf("Frites", "Raviolis aux aubergines")
            )
        )
    ),
    Meal(
        idMeal = "3",
        typeMeal = "Dîner",
        foodies = listOf(
            Foodie(
                type = "Plat",
                content = listOf("Frites", "Raviolis aux aubergines")
            )
        )
    )
)

private val mockRestaurants = listOf(
    Restaurant(
        id = "1",
        name = "Brasserie Boutonnet",
        url = "https://example.com",
        hours = "07:30 - 22:00",
        gpsCoord = GpsCoord(43.623478, 3.869285),
        isFavorite = true,
        crowdLevel = CrowdLevel.HIGH
    ),
    Restaurant(
        id = "2",
        name = "Restaurant de la street",
        url = "https://example.com",
        hours = "11:30 - 14:00",
        gpsCoord = GpsCoord(43.631014, 3.860346),
        isFavorite = true,
        crowdLevel = CrowdLevel.MEDIUM
    )
)

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val mockRestaurantsWithMeals = mockRestaurants.map { restaurant ->
        RestaurantWithMeals(restaurant, mockMeals)
    }

    HomeScreenContent(
        favoriteRestaurantsWithMeals = mockRestaurantsWithMeals,
        onRestaurantClick = {},
        onFavoriteClick = {}
    )
}
