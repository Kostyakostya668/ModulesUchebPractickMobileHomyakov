package com.example.collegeschedulehomyakov

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.collegeschedulehomyakov.data.repository.FavoritesRepository
import com.example.collegeschedulehomyakov.ui.favotites.FavoritesScreen
import com.example.collegeschedulehomyakov.ui.schedule.ScheduleScreen
import com.example.collegeschedulehomyakov.ui.theme.CollegeScheduleHomyakovTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var favoritesRepository: FavoritesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        favoritesRepository = FavoritesRepository(this)

        setContent {
            CollegeScheduleHomyakovTheme {
//                ApiTestScreen()
                CollegeScheduleApp(favoritesRepository = favoritesRepository)
            }
        }
    }
}

enum class AppDestination {
    SCHEDULE, FAVORITES
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollegeScheduleApp(favoritesRepository: FavoritesRepository) {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestination.SCHEDULE) }
    var selectedGroupFromFavorites by remember { mutableStateOf<String?>(null) }
    val favorites by favoritesRepository.favorites.collectAsState(emptySet())

    Scaffold(
        bottomBar = {
            NavigationBar(
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, "Расписание") },
                    label = { Text("Расписание") },
                    selected = currentDestination == AppDestination.SCHEDULE,
                    onClick = { currentDestination = AppDestination.SCHEDULE }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Favorite, "Избранное") },
                    label = { Text("Избранное") },
                    selected = currentDestination == AppDestination.FAVORITES,
                    onClick = { currentDestination = AppDestination.FAVORITES }
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (currentDestination) {
                AppDestination.SCHEDULE -> ScheduleScreen(
                    favoritesRepository = favoritesRepository,
                    initialGroup = selectedGroupFromFavorites,
                    onGroupSelected = { groupName ->
                    }
                )
                AppDestination.FAVORITES -> FavoritesScreen(
                    favoritesRepository = favoritesRepository,
                    onGroupSelected = { groupName ->
                        selectedGroupFromFavorites = groupName
                        currentDestination = AppDestination.SCHEDULE
                    }
                )
            }
        }
    }
}