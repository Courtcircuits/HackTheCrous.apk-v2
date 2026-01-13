package xyz.courtcircuit.hackthecrous.domain.model

data class Meal(
    val idMeal: String,
    val typeMeal: String,
    val foodies: List<Foodie>
)

data class Foodie(
    val type: String,
    val content: List<String>
)
