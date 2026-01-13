package xyz.courtcircuit.hackthecrous.domain.model

enum class RestaurantType {
    ALL,
    RESTO,
    CAFET,
    BRASSERIE;

    companion object {
        fun fromRestaurantName(name: String): RestaurantType {
            return when {
                name.contains("Resto", ignoreCase = true) -> RESTO
                name.contains("Cafet", ignoreCase = true) -> CAFET
                name.contains("Brasserie", ignoreCase = true) -> BRASSERIE
                else -> ALL
            }
        }
    }
}
