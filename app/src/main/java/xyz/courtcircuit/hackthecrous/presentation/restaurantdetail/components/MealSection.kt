package xyz.courtcircuit.hackthecrous.presentation.restaurantdetail.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import xyz.courtcircuit.hackthecrous.domain.model.Foodie

@Composable
fun MealSection(
    mealType: String,
    timeSlot: String,
    foodies: List<Foodie>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Time slot
        Text(
            text = timeSlot,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Meal type title (Déjeuner, Dîner)
        Text(
            text = mealType,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontSize = 28.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Foodie sections
        foodies.forEach { foodie ->
            FoodieCategory(
                categoryName = foodie.type,
                items = foodie.content
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun FoodieCategory(
    categoryName: String,
    items: List<String>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // Category title
        Text(
            text = categoryName,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Items list
        items.forEach { item ->
            MenuItem(text = item)
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
fun MenuItem(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = "• $text",
        style = MaterialTheme.typography.bodyMedium,
        color = Color.Gray,
        fontSize = 15.sp,
        modifier = modifier.padding(start = 8.dp)
    )
}
