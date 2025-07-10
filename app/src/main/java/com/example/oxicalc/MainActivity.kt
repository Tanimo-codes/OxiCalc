package com.example.oxicalc

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.oxicalc.model.readOnboardingState
import com.example.oxicalc.navigation.Screen
import com.example.oxicalc.navigation.SetupNavGraph
import com.example.oxicalc.ui.theme.OxiCalcTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

private val Context.dataStore by preferencesDataStore(name = "settings")
private val ONBOARDING_KEY = booleanPreferencesKey("onboarding_completed")

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        installSplashScreen()

        lifecycleScope.launch {
            val completed = readOnboardingState(applicationContext).first()
            val startDest = if (completed) Screen.Home.route else Screen.Onboarding.route

            setContent {
                OxiCalcTheme {
                    val navController = rememberNavController()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route
                    val showBottomBar = currentRoute != Screen.Onboarding.route

                    var selectedItem by remember { mutableIntStateOf(0) }
                    val items = listOf("Home", "History", "Rules")
                    val routes = listOf(Screen.Home.route, Screen.History.route, Screen.Rules.route)
                    val selectedIcons = listOf(
                        Icons.Filled.Home,
                        Icons.Filled.History,
                        Icons.Filled.Info
                    )
                    val unselectedIcons = listOf(
                        Icons.Outlined.Home,
                        Icons.Outlined.History,
                        Icons.Outlined.Info
                    )

                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        bottomBar = {
                            if (showBottomBar) {
                                NavigationBar {
                                    items.forEachIndexed { index, item ->
                                        NavigationBarItem(
                                            icon = {
                                                Icon(
                                                    if (selectedItem == index) selectedIcons[index]
                                                    else unselectedIcons[index],
                                                    contentDescription = item
                                                )
                                            },
                                            label = { Text(item) },
                                            selected = selectedItem == index,
                                            onClick = {
                                                selectedItem = index
                                                navController.navigate(routes[index]) {
                                                    popUpTo(Screen.Home.route) {
                                                        saveState = true
                                                    }
                                                    launchSingleTop = true
                                                    restoreState = true
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    ) { innerPadding ->
                        SetupNavGraph(
                            navController = navController,
                            modifier = Modifier.padding(innerPadding),
                            startDestination = startDest
                        )
                    }
                }
            }
        }
    }
}
