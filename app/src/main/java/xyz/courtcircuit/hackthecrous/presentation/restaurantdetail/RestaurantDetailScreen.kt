package xyz.courtcircuit.hackthecrous.presentation.restaurantdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import xyz.courtcircuit.hackthecrous.domain.model.Foodie
import xyz.courtcircuit.hackthecrous.domain.model.GpsCoord
import xyz.courtcircuit.hackthecrous.domain.model.Meal
import xyz.courtcircuit.hackthecrous.domain.model.Restaurant
import xyz.courtcircuit.hackthecrous.presentation.restaurantdetail.components.MealSection
import xyz.courtcircuit.hackthecrous.ui.theme.HackthecrousTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantDetailScreen(
    viewModel: RestaurantDetailViewModel = hiltViewModel()
) {
    val restaurant by viewModel.restaurant.collectAsState()
    val meals by viewModel.meals.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    restaurant?.let { restaurantData ->
        RestaurantDetailContent(
            restaurant = restaurantData,
            meals = meals,
            isLoading = isLoading,
            onRefresh = viewModel::refresh
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantDetailContent(
    restaurant: Restaurant,
    meals: List<Meal>,
    isLoading: Boolean,
    onRefresh: () -> Unit
) {
    PullToRefreshBox(
        isRefreshing = isLoading,
        onRefresh = onRefresh,
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1C1C1C))
        ) {
            // Restaurant header
            item {
                RestaurantHeader(restaurant = restaurant)
            }

            // Meals sections
            items(meals) { meal ->
                MealSection(
                    mealType = formatMealType(meal.typeMeal),
                    timeSlot = getTimeSlotForMealType(meal.typeMeal),
                    foodies = meal.foodies
                )
            }

            // Map placeholder
            item {
                MapPlaceholder()
            }
        }
    }
}

@Composable
fun RestaurantHeader(
    restaurant: Restaurant,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Restaurant name
        Text(
            text = restaurant.name,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontSize = 32.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Status and distance
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Status badge
            StatusBadge(text = "Ouvert", isOpen = true)

            // Distance badge
            DistanceBadge(distance = restaurant.distance ?: 0.09)
        }
    }
}

@Composable
fun StatusBadge(
    text: String,
    isOpen: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isOpen) Color(0xFF00C853).copy(alpha = 0.2f) else Color.Gray.copy(alpha = 0.2f)
        )
    ) {
        Text(
            text = text,
            color = if (isOpen) Color(0xFF00C853) else Color.Gray,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@Composable
fun DistanceBadge(
    distance: Double,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Gray.copy(alpha = 0.2f)
        )
    ) {
        Text(
            text = "${String.format("%.2f", distance)}km",
            color = Color.Gray,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@Composable
fun MapPlaceholder(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(Color.Gray.copy(alpha = 0.3f))
            .blur(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Trajet",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Carte disponible prochainement",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    }
}

// Helper functions
private fun formatMealType(typeMeal: String): String {
    return when (typeMeal.lowercase()) {
        "déjeuner", "dejeuner" -> "Déjeuner"
        "dîner", "diner" -> "Dîner"
        "petit-déjeuner", "petit-dejeuner" -> "Petit-déjeuner"
        else -> typeMeal.replaceFirstChar { it.uppercase() }
    }
}

private fun getTimeSlotForMealType(typeMeal: String): String {
    return when (typeMeal.lowercase()) {
        "déjeuner", "dejeuner" -> "11h30\n13h30"
        "dîner", "diner" -> "19h30\n21h30"
        "petit-déjeuner", "petit-dejeuner" -> "07h30\n09h00"
        else -> ""
    }
}

// Preview
private val mockMeals = listOf(
    Meal(
        idMeal = "1",
        typeMeal = "Déjeuner",
        foodies = listOf(
            Foodie(
                type = "Bar à salade",
                content = listOf("Assortiment de crudités")
            ),
            Foodie(
                type = "Plats",
                content = listOf(
                    "Poisson meunière",
                    "Cuisse de poulet",
                    "Gnocchis"
                )
            ),
            Foodie(
                type = "Accompagnements",
                content = listOf(
                    "Frites",
                    "Macaronis",
                    "Ratatouille"
                )
            ),
            Foodie(
                type = "Bar à dessert",
                content = listOf("Assortiment de desserts")
            )
        )
    ),
    Meal(
        idMeal = "2",
        typeMeal = "Dîner",
        foodies = listOf(
            Foodie(
                type = "Bar à salade",
                content = listOf("menu non communiqué")
            ),
            Foodie(
                type = "Plats",
                content = listOf("Brasserie fermée")
            ),
            Foodie(
                type = "Accompagnements",
                content = listOf("menu non communiqué")
            ),
            Foodie(
                type = "Bar à dessert",
                content = listOf("menu non communiqué")
            )
        )
    )
)

@Preview(showBackground = true)
@Composable
fun RestaurantDetailScreenPreview() {
    HackthecrousTheme {
        RestaurantDetailContent(
            restaurant = Restaurant(
                id = "1",
                name = "Brasserie Boutonnet",
                url = "https://example.com",
                hours = "07:30 - 22:00",
                gpsCoord = GpsCoord(43.623478, 3.869285),
                isFavorite = true,
                distance = 0.09
            ),
            meals = mockMeals,
            isLoading = false,
            onRefresh = {}
        )
    }
}
