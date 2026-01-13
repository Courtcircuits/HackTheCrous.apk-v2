package xyz.courtcircuit.hackthecrous

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint
import xyz.courtcircuit.hackthecrous.presentation.navigation.MainScreen
import xyz.courtcircuit.hackthecrous.ui.theme.HackthecrousTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HackthecrousTheme {
                MainScreen()
            }
        }
    }
}

@Preview(showBackground = true, name = "App with Bottom Nav")
@Composable
fun AppPreview() {
    HackthecrousTheme {
        // This preview shows the full app including bottom navigation
        // See MainScreen.kt for more preview variations (Home, Search, Restaurants tabs)
        xyz.courtcircuit.hackthecrous.presentation.navigation.MainScreenPreviewContent()
    }
}