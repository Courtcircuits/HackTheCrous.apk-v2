package xyz.courtcircuit.hackthecrous.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import xyz.courtcircuit.hackthecrous.domain.model.GpsCoord
import xyz.courtcircuit.hackthecrous.domain.model.Restaurant
import xyz.courtcircuit.hackthecrous.domain.model.RestaurantType
import xyz.courtcircuit.hackthecrous.presentation.home.HomeScreen
import xyz.courtcircuit.hackthecrous.presentation.restaurant.RestaurantScreen
import xyz.courtcircuit.hackthecrous.presentation.restaurant.RestaurantScreenContent
import xyz.courtcircuit.hackthecrous.presentation.restaurantdetail.RestaurantDetailScreen
import xyz.courtcircuit.hackthecrous.presentation.search.SearchScreen
import xyz.courtcircuit.hackthecrous.ui.theme.HackthecrousTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Determine if we should show back button
    val showBackButton = currentRoute?.startsWith("restaurant_detail") == true

    // Determine title based on route
    val title = when {
        currentRoute?.startsWith("restaurant_detail") == true -> ""
        currentRoute == BottomNavItem.Restaurants.route -> "Restaurant"
        currentRoute == BottomNavItem.Home.route -> "Accueil"
        currentRoute == BottomNavItem.Search.route -> "Recherche"
        else -> ""
    }

    Scaffold(
        topBar = {
            if (currentRoute != null) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = title,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        if (showBackButton) {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = Color.White
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color(0xFF1C1C1C)
                    ),
                    modifier = Modifier.statusBarsPadding()
                )
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF1C1C1C)
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                bottomNavItems.forEach { item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label
                            )
                        },
                        label = { Text(item.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF00C853),
                            selectedTextColor = Color.White,
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Restaurants.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(BottomNavItem.Home.route) {
                HomeScreen(
                    onRestaurantClick = { restaurantId ->
                        navController.navigate("restaurant_detail/$restaurantId")
                    }
                )
            }
            composable(BottomNavItem.Search.route) {
                SearchScreen(
                    onRestaurantClick = { restaurantId ->
                        navController.navigate("restaurant_detail/$restaurantId")
                    }
                )
            }
            composable(BottomNavItem.Restaurants.route) {
                RestaurantScreen(
                    onRestaurantClick = { restaurantId ->
                        navController.navigate("restaurant_detail/$restaurantId")
                    }
                )
            }
            composable("restaurant_detail/{restaurantId}") {
                RestaurantDetailScreen()
            }
        }
    }
}

// Preview version with mock data
@Composable
fun MainScreenPreviewContent(
    selectedRoute: String = BottomNavItem.Restaurants.route
) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF1C1C1C)
            ) {
                bottomNavItems.forEach { item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label
                            )
                        },
                        label = { Text(item.label) },
                        selected = selectedRoute == item.route,
                        onClick = { },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF00C853),
                            selectedTextColor = Color.White,
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (selectedRoute) {
                BottomNavItem.Home.route -> HomeScreen(onRestaurantClick = {})
                BottomNavItem.Search.route -> SearchScreen(onRestaurantClick = {})
                BottomNavItem.Restaurants.route -> {
                    RestaurantScreenContent(
                        restaurants = listOf(
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
                            )
                        ),
                        selectedTab = RestaurantType.ALL,
                        isLoading = false,
                        onRestaurantClick = {},
                        onTabSelected = {},
                        onFavoriteClick = {},
                        onRefresh = {}
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Main Screen - Restaurants Tab")
@Composable
fun MainScreenRestaurantsPreview() {
    HackthecrousTheme {
        MainScreenPreviewContent(selectedRoute = BottomNavItem.Restaurants.route)
    }
}

@Preview(showBackground = true, name = "Main Screen - Home Tab")
@Composable
fun MainScreenHomePreview() {
    HackthecrousTheme {
        MainScreenPreviewContent(selectedRoute = BottomNavItem.Home.route)
    }
}

@Preview(showBackground = true, name = "Main Screen - Search Tab")
@Composable
fun MainScreenSearchPreview() {
    HackthecrousTheme {
        MainScreenPreviewContent(selectedRoute = BottomNavItem.Search.route)
    }
}