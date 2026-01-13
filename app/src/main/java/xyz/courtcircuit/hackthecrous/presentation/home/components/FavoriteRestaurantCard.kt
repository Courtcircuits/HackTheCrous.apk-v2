package xyz.courtcircuit.hackthecrous.presentation.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import xyz.courtcircuit.hackthecrous.domain.model.CrowdLevel
import xyz.courtcircuit.hackthecrous.domain.model.Foodie
import xyz.courtcircuit.hackthecrous.domain.model.GpsCoord
import xyz.courtcircuit.hackthecrous.domain.model.Meal
import xyz.courtcircuit.hackthecrous.domain.model.Restaurant

@Composable
fun FavoriteRestaurantCard(
    restaurant: Restaurant,
    meals: List<Meal>,
    onClick: () -> Unit,
    onFavoriteClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = restaurant.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    color = Color.White
                )

                IconButton(
                    onClick = { onFavoriteClick(restaurant.id) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Bookmark,
                        contentDescription = "Remove from favorites",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Vous avez marqué comme favoris ce restaurant",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Group meals by type and display them
            val mealsByType = meals.groupBy { it.typeMeal }

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                mealsByType.entries.take(3).forEachIndexed { index, (mealType, mealsOfType) ->
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = mealType,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        mealsOfType.firstOrNull()?.foodies?.forEach { foodie ->
                            foodie.content.take(2).forEach { item ->
                                Text(
                                    text = "• $item",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Group,
                    contentDescription = "Crowd level",
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = when (restaurant.crowdLevel) {
                        CrowdLevel.LOW -> "Peu fréquenté"
                        CrowdLevel.MEDIUM -> "Modéré"
                        CrowdLevel.HIGH -> "Crowded"
                        CrowdLevel.UNKNOWN -> "Crowded"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 4.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Icon(
                    imageVector = Icons.Default.Place,
                    contentDescription = "Distance",
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = restaurant.distance?.let { "${String.format("%.1f", it)}km" } ?: "5km",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 4.dp)
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

private val mockRestaurant = Restaurant(
    id = "1",
    name = "Brasserie Boutonnet",
    url = "https://example.com",
    hours = "07:30 - 22:00",
    gpsCoord = GpsCoord(43.623478, 3.869285),
    isFavorite = true
)

@Preview
@Composable
fun FavoriteRestaurantCardPreview() {
    FavoriteRestaurantCard(
        restaurant = mockRestaurant,
        meals = mockMeals,
        onClick = {},
        onFavoriteClick = {}
    )
}
